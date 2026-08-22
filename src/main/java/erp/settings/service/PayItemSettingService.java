package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import erp.settings.dao.DeductItemDao;
import erp.settings.dao.PayItemDao;
import erp.settings.dao.TaxFreeItemDao;
import erp.settings.dto.PayItemRow;
import erp.settings.model.DeductItem;
import erp.settings.model.PayItem;
import erp.settings.model.TaxFreeItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 지급항목설정 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 支給項目設定の業務ルールとデータ変更トランザクションを処理する。
public class PayItemSettingService {
	private static final Set<String> REQUIRED_PAY_ITEM_NAMES =
			new HashSet<>(Arrays.asList("기본급"));
	private static final Set<String> REQUIRED_DEDUCT_ITEM_NAMES =
			new HashSet<>(Arrays.asList("국민연금", "건강보험", "장기요양보험",
					"노인장기요양보험", "고용보험", "소득세", "지방소득세"));

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static PayItemSettingService instance = new PayItemSettingService();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static PayItemSettingService getInstance() {
		return instance;
	}

	// 외부 객체 생성 방지 처리
	// 전달받은 값으로 지급항목설정 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で支給項目設定オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private PayItemSettingService() {
	}

	// 도메인별 DAO 연동 객체 할당 (DAO 클래스가 구현되어 있다고 가정)
	// 担当業務を処理するServiceまたはDAOを共有フィールドへ割り当て、各処理から再利用する。
	private PayItemDao payItemDao = PayItemDao.getInstance();
	private DeductItemDao deductItemDao = DeductItemDao.getInstance();
	private TaxFreeItemDao taxFreeItemDao = TaxFreeItemDao.getInstance();

	/**
	 * 서버 시작 시 급여 계산에 필요한 기준 항목이 없을 때만 생성한다.
	  * 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	 */
	// 지급항목설정 처리에 사용할 필수값항목 목록 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 支給項目設定処理で使用する必須値項目一覧データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void initializeRequiredItems() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Set<String> payNames = new HashSet<>();
			for (PayItem item : payItemDao.selectAll(conn)) {
				payNames.add(item.getPayName());
			}
			if (!payNames.contains("기본급")) {
				payItemDao.insert(conn, createRequiredPayItem());
			}

			Set<String> deductNames = new HashSet<>();
			for (DeductItem item : deductItemDao.selectAll(conn)) {
				deductNames.add(item.getDeductName());
			}
			for (DeductItem item : createRequiredDeductItems()) {
				if (!deductNames.contains(item.getDeductName())) {
					deductItemDao.insert(conn, item);
				}
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("필수 급여항목 초기화 중 오류 발생", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 지급항목설정 처리에 사용할 필수값지급항목 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 支給項目設定処理で使用する必須値支給項目データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private PayItem createRequiredPayItem() {
		PayItem item = new PayItem();
		item.setPayName("기본급");
		item.setTaxType("전체과세");
		item.setTaxFreeLimit(0L);
		item.setCalcMethod("社員基本給");
		item.setRoundUnit(10);
		item.setUseYn("Y");
		return item;
	}

	// 지급항목설정 처리에 사용할 필수값공제항목 목록 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 支給項目設定処理で使用する必須値控除項目一覧データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private List<DeductItem> createRequiredDeductItems() {
		return Arrays.asList(
				createRequiredDeductItem("국민연금", "標準報酬月額 × 4.5%", "従業員負担分"),
				createRequiredDeductItem("건강보험", "標準報酬月額 × 3.545%", "従業員負担分"),
				createRequiredDeductItem("장기요양보험", "健康保険料 × 介護保険料率", "従業員負担分"),
				createRequiredDeductItem("고용보험", "標準報酬月額 × 0.9%", "従業員負担分"),
				createRequiredDeductItem("소득세", "給与所得の源泉徴収税額表", "給与所得税"),
				createRequiredDeductItem("지방소득세", "所得税 × 10%", "地方所得税"));
	}

	// 지급항목설정 처리에 사용할 필수값공제항목 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 支給項目設定処理で使用する必須値控除項目データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private DeductItem createRequiredDeductItem(String name, String calcMethod, String note) {
		DeductItem item = new DeductItem();
		item.setDeductName(name);
		item.setCalcMethod(calcMethod);
		item.setRoundUnit(10);
		item.setNote(note);
		item.setUseYn("Y");
		return item;
	}

	// ==========================================
	// 1. 지급항목(PAY_ITEM) 관련 로직
	// 支給項目の一覧照会・単件照会・登録・更新・削除に関する処理をまとめる。
	// ==========================================

	/**
	 * 지급항목 전체 리스트 조회 (비과세명, 근태항목명 조인 결과 포함)
	 *
	 * @return 지급항목 조인 DTO 목록 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 지급항목설정 처리에 필요한 지급항목행 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 支給項目設定処理に必要な支給項目行一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<PayItemRow> getPayItemRows() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return payItemDao.selectPayItemRows(conn);
		} catch (SQLException e) {
			throw new RuntimeException("지급항목 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * 특정 지급항목 단건 조회 (에디터 패널 표시용)
	  * 識別番号に該当する一件の詳細データを照会し、編集・詳細表示に使用する。
	 *
	 * @param payItemId 지급항목 식별 번호
	 * @return 해당 지급항목 모델 객체 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 지급항목설정 처리에 필요한 지급항목를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 支給項目設定処理に必要な支給項目を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public PayItem getPayItem(int payItemId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return payItemDao.selectById(conn, payItemId);
		} catch (SQLException e) {
			throw new RuntimeException("지급항목 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 삭제 확인창을 열기 전에 필수 지급항목인지 확인한다.
	// 필수값지급항목 조건의 충족 여부를 확인하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 必須値支給項目条件を満たしているか確認して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public boolean isRequiredPayItem(int payItemId) {
		PayItem item = getPayItem(payItemId);
		return item != null && REQUIRED_PAY_ITEM_NAMES.contains(item.getPayName());
	}

	/**
	 * 지급항목 삽입, 수정, 삭제 트랜잭션 분기 및 무결성 검증 처리
	  * 複数のデータ変更を一つのトランザクションとして処理し、成功時はコミット、失敗時はロールバックする。
	 *
	 * @param item   지급항목 모델 객체
	 * @param action 실행할 액션 (insert, update, delete)
	  * @param action 処理に必要な値を受け取る。
	 */
	// 요청에서 지급항목작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// リクエストから支給項目処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public void processPayItemAction(PayItem item, String action) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 수동 커밋 모드 전환 처리

			// 기본급은 급여 자동 산정에 필요한 기준 항목이므로 이름 변경과 삭제를 막는다.
			// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
			if ("update".equals(action) || "delete".equals(action)) {
				PayItem savedItem = payItemDao.selectById(conn, item.getPayItemId());
				if (savedItem != null && REQUIRED_PAY_ITEM_NAMES.contains(savedItem.getPayName())) {
					throw new IllegalArgumentException("必須支給項目は修正または削除できません。");
				}
			}

			// 과세 구분에 따라 비과세 코드와 법정 한도액을 정규화한다.
			// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
			if ("전체과세".equals(item.getTaxType())) {
				item.setTaxFreeCode(null);
				item.setTaxFreeLimit(0L);
			} else if (!"delete".equals(action)) {
				if ("DIRECT".equals(item.getTaxFreeCode())) {
					String directName = item.getDirectTaxFreeName();
					if (directName == null || directName.trim().isEmpty()) {
						throw new IllegalArgumentException("직접입력 비과세명을 입력해 주세요.");
					}
					TaxFreeItem directItem = new TaxFreeItem();
					directItem.setTaxFreeCode(taxFreeItemDao.selectNextUserCode(conn));
					directItem.setLegalClause("직접입력");
					directItem.setReportField("직접입력");
					directItem.setTaxFreeName(directName.trim());
					directItem.setDefaultLimit(item.getDirectTaxFreeLimit() == null ? 0L : item.getDirectTaxFreeLimit());
					directItem.setPayStatementYn("N");
					directItem.setIncomeCategory("비과세");
					taxFreeItemDao.insert(conn, directItem);
					item.setTaxFreeCode(directItem.getTaxFreeCode());
				}

				TaxFreeItem taxFreeItem = taxFreeItemDao.selectByCode(conn, item.getTaxFreeCode());
				if (taxFreeItem == null) {
					throw new IllegalArgumentException("비과세 소득 코드를 선택해 주세요.");
				}
				item.setTaxFreeLimit(taxFreeItem.getDefaultLimit());
			}

			// [데이터 무결성 검증 2] 근태연결 및 일괄지급 속성 자동 매핑 (스키마 CK_PAY_ITEM_METHOD_REL 제약 준수)[cite:
			// 重複値とデータベース制約違反を確認し、保存可能なデータだけを処理する。
			// 13]
			if (item.getLinkAttendId() != null && item.getLinkAttendId() > 0) {
				item.setPayMethod("근태연결");
				item.setBulkPayAmount(null);
			} else if (item.getBulkPayAmount() != null && item.getBulkPayAmount() > 0) {
				item.setPayMethod("일괄지급");
				item.setLinkAttendId(null);
			} else {
				item.setPayMethod(null);
			}

			// 액션 분기 처리
			// 処理区分と現在状態を確認し、条件に合う業務処理だけを実行する。
			if ("insert".equals(action)) {
				payItemDao.insert(conn, item);
			} else if ("update".equals(action)) {
				payItemDao.update(conn, item);
			} else if ("delete".equals(action)) {
				payItemDao.delete(conn, item.getPayItemId());
			}

			conn.commit(); // 트랜잭션 정상 완료 시 커밋 처리
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("지급항목 액션 처리 중 오류 발생", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// ==========================================
	// 2. 공제항목(DEDUCT_ITEM) 관련 로직
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	// ==========================================

	/**
	 * 공제항목 전체 리스트 조회
	 *
	 * @return 공제항목 목록 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 지급항목설정 처리에 필요한 공제항목 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 支給項目設定処理に必要な控除項目一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<DeductItem> getDeductItems() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return deductItemDao.selectAll(conn);
		} catch (SQLException e) {
			throw new RuntimeException("공제항목 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * 특정 공제항목 단건 조회 (에디터 패널 표시용)
	  * 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	 *
	 * @param deductItemId 공제항목 식별 번호
	 * @return 해당 공제항목 모델 객체 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 지급항목설정 처리에 필요한 공제항목를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 支給項目設定処理に必要な控除項目を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public DeductItem getDeductItem(int deductItemId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return deductItemDao.selectById(conn, deductItemId);
		} catch (SQLException e) {
			throw new RuntimeException("공제항목 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 삭제 확인창을 열기 전에 필수 공제항목인지 확인한다.
	// 필수값공제항목 조건의 충족 여부를 확인하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 必須値控除項目条件を満たしているか確認して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public boolean isRequiredDeductItem(int deductItemId) {
		DeductItem item = getDeductItem(deductItemId);
		return item != null && REQUIRED_DEDUCT_ITEM_NAMES.contains(item.getDeductName());
	}

	/**
	 * 공제항목 삽입, 수정, 삭제 트랜잭션 분기 처리
	  * 複数のデータ変更を一つのトランザクションとして処理し、成功時はコミット、失敗時はロールバックする。
	 *
	 * @param item   공제항목 모델 객체
	 * @param action 실행할 액션 (insert, update, delete)
	  * @param action 処理に必要な値を受け取る。
	 */
	// 요청에서 공제항목작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// リクエストから控除項目処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public void processDeductItemAction(DeductItem item, String action) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 수동 커밋 모드 전환 처리

			// 법정 공제 항목은 계산 로직이 이름을 기준으로 사용하므로 변경과 삭제를 막는다.
			// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
			if ("update".equals(action) || "delete".equals(action)) {
				DeductItem savedItem = deductItemDao.selectById(conn, item.getDeductItemId());
				if (savedItem != null && REQUIRED_DEDUCT_ITEM_NAMES.contains(savedItem.getDeductName())) {
					throw new IllegalArgumentException("必須控除項目は修正または削除できません。");
				}
			}

			if ("insert".equals(action)) {
				deductItemDao.insert(conn, item);
			} else if ("update".equals(action)) {
				deductItemDao.update(conn, item);
			} else if ("delete".equals(action)) {
				deductItemDao.delete(conn, item.getDeductItemId());
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("공제항목 액션 처리 중 오류 발생", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// ==========================================
	// 3. 모달용 보조 데이터(TAX_FREE_ITEM) 로직
	// 選択・詳細処理に使用するモーダル領域を構成し、本文スクロール中も見出しを確認できるようにする。
	// ==========================================

	/**
	 * 비과세 항목 전체 리스트 조회 (비과세 항목 선택 모달용)
	 *
	 * @return 비과세 항목 목록 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 지급항목설정 처리에 필요한 세금비과세항목 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 支給項目設定処理に必要な税金非課税項目一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<TaxFreeItem> getTaxFreeItems() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			for (TaxFreeItem item : TaxFreeItemDefaults.create()) {
				taxFreeItemDao.insertIfAbsent(conn, item);
			}
			List<TaxFreeItem> items = taxFreeItemDao.selectAll(conn);
			conn.commit();
			return items;
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("비과세 항목 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/** 비과세 코드에 해당하는 법정 항목을 조회한다. */
	// 지급항목설정 처리에 필요한 세금비과세항목를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 支給項目設定処理に必要な税金非課税項目を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public TaxFreeItem getTaxFreeItem(String taxFreeCode) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return taxFreeItemDao.selectByCode(conn, taxFreeCode);
		} catch (SQLException e) {
			throw new RuntimeException("비과세 항목 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}

package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.settings.dao.DeductItemDao;
import erp.settings.dao.PayItemDao;
import erp.settings.dao.TaxFreeItemDao;
import erp.settings.dto.PayItemRow;
import erp.settings.model.DeductItem;
import erp.settings.model.PayItem;
import erp.settings.model.TaxFreeItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class PayItemSettingService {

	// 싱글톤 인스턴스 생성
	private static PayItemSettingService instance = new PayItemSettingService();

	// 싱글톤 접근 메서드
	public static PayItemSettingService getInstance() {
		return instance;
	}

	// 외부 객체 생성 방지 처리
	private PayItemSettingService() {
	}

	// 도메인별 DAO 연동 객체 할당 (DAO 클래스가 구현되어 있다고 가정)
	private PayItemDao payItemDao = PayItemDao.getInstance();
	private DeductItemDao deductItemDao = DeductItemDao.getInstance();
	private TaxFreeItemDao taxFreeItemDao = TaxFreeItemDao.getInstance();

	// ==========================================
	// 1. 지급항목(PAY_ITEM) 관련 로직
	// ==========================================

	/**
	 * 지급항목 전체 리스트 조회 (비과세명, 근태항목명 조인 결과 포함)
	 *
	 * @return 지급항목 조인 DTO 목록 반환
	 */
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
	 *
	 * @param payItemId 지급항목 식별 번호
	 * @return 해당 지급항목 모델 객체 반환
	 */
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

	/**
	 * 지급항목 삽입, 수정, 삭제 트랜잭션 분기 및 무결성 검증 처리
	 *
	 * @param item   지급항목 모델 객체
	 * @param action 실행할 액션 (insert, update, delete)
	 */
	public void processPayItemAction(PayItem item, String action) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 수동 커밋 모드 전환 처리

			// 과세 구분에 따라 비과세 코드와 법정 한도액을 정규화한다.
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
	// ==========================================

	/**
	 * 공제항목 전체 리스트 조회
	 *
	 * @return 공제항목 목록 반환
	 */
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
	 *
	 * @param deductItemId 공제항목 식별 번호
	 * @return 해당 공제항목 모델 객체 반환
	 */
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

	/**
	 * 공제항목 삽입, 수정, 삭제 트랜잭션 분기 처리
	 *
	 * @param item   공제항목 모델 객체
	 * @param action 실행할 액션 (insert, update, delete)
	 */
	public void processDeductItemAction(DeductItem item, String action) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 수동 커밋 모드 전환 처리

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
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// ==========================================
	// 3. 모달용 보조 데이터(TAX_FREE_ITEM) 로직
	// ==========================================

	/**
	 * 비과세 항목 전체 리스트 조회 (비과세 항목 선택 모달용)
	 *
	 * @return 비과세 항목 목록 반환
	 */
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

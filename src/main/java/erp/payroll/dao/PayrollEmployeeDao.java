package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.PayrollEmployee;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원별 급여결과를 저장하고 조회한다.
// 급여사원 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 給与社員データをデータベースから照会し、登録・更新・削除する。
public class PayrollEmployeeDao {

    // 싱글톤 인스턴스 생성
    // 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
    private static PayrollEmployeeDao payrollEmployeeDao = new PayrollEmployeeDao();

    // 싱글톤 접근 메서드
    // 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
    // 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
    // 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
    // オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
    public static PayrollEmployeeDao getInstance() {
        return payrollEmployeeDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    // 전달받은 값으로 급여사원 객체의 초기 상태를 구성한다.
    // 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
    // 受け取った値で給与社員オブジェクトの初期状態を構成する。
    // 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
    private PayrollEmployeeDao() {}

    // 사원별 급여결과 등록
    // 전달받은 급여사원 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
    // 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
    // 受け取った給与社員データをデータベースへ登録し、処理件数を返す。
    // 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
    public void insert(Connection conn, PayrollEmployee pe) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO PAYROLL_EMPLOYEE (PAYROLL_EMPLOYEE_ID, PAYROLL_RUN_ID, EMPLOYEE_ID) "
                       + "VALUES (PAYROLL_EMPLOYEE_SEQ.NEXTVAL, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pe.getPayrollRunId());
            pstmt.setInt(2, pe.getEmployeeId());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 급여결과 단건 조회
    // 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
    // Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
    // 検索条件に合うBy識別番号データをデータベースから照会する。
    // Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
    public PayrollEmployee selectById(Connection conn, int peId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_EMPLOYEE_ID, PAYROLL_RUN_ID, EMPLOYEE_ID "
                       + "FROM PAYROLL_EMPLOYEE WHERE PAYROLL_EMPLOYEE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, peId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makePayrollEmployeeFromResultSet(rs);
            }
            return null;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 회차의 대상 사원 목록 조회
    // 조회 조건에 맞는 By급여 회차식별번호 데이터를 데이터베이스에서 조회한다.
    // Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
    // 検索条件に合うBy給与回次識別番号データをデータベースから照会する。
    // Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
    public List<PayrollEmployee> selectByRunId(Connection conn, int runId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_EMPLOYEE_ID, PAYROLL_RUN_ID, EMPLOYEE_ID "
                       + "FROM PAYROLL_EMPLOYEE WHERE PAYROLL_RUN_ID = ? ORDER BY EMPLOYEE_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, runId);
            rs = pstmt.executeQuery();
            
            List<PayrollEmployee> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makePayrollEmployeeFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 급여결과 수정
    // 식별조건에 해당하는 급여사원 데이터를 전달받은 값으로 수정한다.
    // 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
    // 識別条件に該当する給与社員データを受け取った値で更新する。
    // 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
    public int update(Connection conn, PayrollEmployee pe) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE PAYROLL_EMPLOYEE SET PAYROLL_RUN_ID = ?, EMPLOYEE_ID = ? "
                       + "WHERE PAYROLL_EMPLOYEE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pe.getPayrollRunId());
            pstmt.setInt(2, pe.getEmployeeId());
            pstmt.setInt(3, pe.getPayrollEmployeeId());
            
            return pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 급여결과 삭제
    // 선택되거나 식별된 급여사원 데이터를 삭제하고 관련 상태를 정리한다.
    // 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
    // 選択または識別された給与社員データを削除し、関連状態を整理する。
    // 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
    public int delete(Connection conn, int peId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM PAYROLL_EMPLOYEE WHERE PAYROLL_EMPLOYEE_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, peId);
            return pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원 삭제 시 해당 사원에게 연결된 급여결과를 함께 삭제
    // 선택되거나 식별된 By사원식별번호 데이터를 삭제하고 관련 상태를 정리한다.
    // 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
    // 選択または識別されたBy社員識別番号データを削除し、関連状態を整理する。
    // 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
    public int deleteByEmployeeId(Connection conn, int employeeId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM PAYROLL_EMPLOYEE WHERE EMPLOYEE_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            return pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 PayrollEmployee 객체로 변환
    // 조회값과 입력값을 조합하여 급여사원From처리결과Set 처리 데이터를 구성한다.
    // SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
    // 照会値と入力値を組み合わせて給与社員From処理結果Setの処理データを構成する。
    // SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
    private PayrollEmployee makePayrollEmployeeFromResultSet(ResultSet rs) throws SQLException {
        PayrollEmployee pe = new PayrollEmployee();
        
        pe.setPayrollEmployeeId(rs.getInt("PAYROLL_EMPLOYEE_ID"));
        pe.setPayrollRunId(rs.getInt("PAYROLL_RUN_ID"));
        pe.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
        
        return pe;
    }
}

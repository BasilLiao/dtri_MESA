package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.Workstation;

public interface WorkstationDao extends JpaRepository<Workstation, Long> {

	// 查詢全部
	ArrayList<Workstation> findAll();

	// 查詢全部 指定工作站代表
	ArrayList<Workstation> findAllByWgidAndSysheaderOrderBySyssortAsc(Long wgid, Boolean sysheader);

	ArrayList<Workstation> findAllByWgidOrderBySyssortAsc(Long wgid);

	// 查詢工作站代表
	ArrayList<Workstation> findAllBySysheader(Boolean sysheader, Pageable pageable);

	// 查詢工作站碼
	ArrayList<Workstation> findAllByWcname(String wcname, Pageable pageable);

	// 查詢工作站 欄位
	ArrayList<Workstation> findAllByWpbcell(String wpbcell, Pageable pageable);

	// 查詢工作站碼+排除自己 欄位
	ArrayList<Workstation> findAllByWcnameAndWcnameNot(String wcname, String wcname2, Pageable pageable);

	// 查詢工作站+排除自己 欄位
	ArrayList<Workstation> findAllByWpbcellAndWpbcellNot(String wpbcell, String wpbcell2, Pageable pageable);

	// 查詢一部分
	@Query("SELECT c FROM Workstation c "//
			+ "WHERE (:wsgname is null or c.wsgname LIKE %:wsgname% ) and "//
			+ "(:wpbname is null or c.wpbname LIKE %:wpbname% ) and "//
			+ "( c.sysstatus = :sysstatus ) and "//
			+ "( c.wid != 0 )  "//
			+ "order by c.wgid asc,c.sysheader desc,c.syssort asc")
	ArrayList<Workstation> findAllByWorkstation(@Param("wsgname") String w_sg_name, @Param("wpbname") String w_pb_name,
			@Param("sysstatus") Integer sysstatus, Pageable pageable);

	// 取得最新G_ID
	@Query(value = "SELECT NEXTVAL('workstation_g_seq')", nativeQuery = true)
	Long getProduction_workstation_g_seq();

	// 取得ID
	@Query(value = "SELECT CURRVAL('workstation_seq')", nativeQuery = true)
	Long getProduction_workstation_seq();

	// delete
	Long deleteByWidAndSysheader(Long id, Boolean sysheader);

	// delete 群組移除
	Long deleteByWgid(Long wgid);
}
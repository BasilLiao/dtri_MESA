package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.WorkstationProgram;

public interface WorkstationProgramDao extends JpaRepository<WorkstationProgram, Long> {

	// 查詢全部
	ArrayList<WorkstationProgram> findAll();

	// 查詢全部 wp_name or wp_c_name
	ArrayList<WorkstationProgram> findAllByWpcnameOrWpname(String wp_c_name, String wp_name);

	// 查詢全部 wp_c_name + 排除自己
	ArrayList<WorkstationProgram> findAllByWpcnameAndWpcnameNot(String wp_c_name, String wp_c_name2);

	// 查詢全部 wp_name + 排除自己
	ArrayList<WorkstationProgram> findAllByWpnameAndWpnameNot(String wp_name, String wp_name2);

	// 查詢全部 By Group
	ArrayList<WorkstationProgram> findAllByWpgidOrderBySyssortAsc(Long wp_g_id);

	// 查詢全部 By Group 排除代表
	ArrayList<WorkstationProgram> findAllByWpgidAndSysheaderOrderBySyssortAsc(Long wp_g_id, Boolean sysheader);

	// 查詢全部 By Group + 特定工作站
	ArrayList<WorkstationProgram> findAllByWpgidAndWpwgidAndSysheaderOrderBySyssortAsc(Long wp_g_id, Long wp_w_g_id, Boolean sysheader);

	// 查詢全部 By Group 代表
	ArrayList<WorkstationProgram> findAllBySysheader(Boolean sysheader);

	// 查詢全部 By wp_w_g_id 關聯工作站
	ArrayList<WorkstationProgram> findAllByWpwgid(Long Wpwgid);

	// 查詢一部分
	@Query("SELECT c FROM WorkstationProgram c "
			+ "WHERE (:wpname is null or c.wpname LIKE %:wpname% ) and (:wpcname is null or c.wpcname LIKE %:wpcname% ) and ( c.sysstatus = :sysstatus )  "
			+ "order by c.wpgid asc,c.sysheader desc,c.syssort asc")
	ArrayList<WorkstationProgram> findAllByProgram(@Param("wpname") String wp_name, @Param("wpcname") String wp_c_name,
			@Param("sysstatus") Integer sysstatus, Pageable pageable);

	// 取得G_ID WORKSTATION_PROGRAM_G_SEQ
	@Query(value = "SELECT NEXTVAL('workstation_program_g_seq')", nativeQuery = true)
	Long getWorkstation_program_g_seq();

	// 取得ID
	@Query(value = "SELECT CURRVAL('workstation_program_seq')", nativeQuery = true)
	Long getWorkstation_program_seq();

	// delete
	Long deleteByWpidAndSysheader(Long id, Boolean sysheader);

	// delete group
	Long deleteByWpgid(Long id);
}
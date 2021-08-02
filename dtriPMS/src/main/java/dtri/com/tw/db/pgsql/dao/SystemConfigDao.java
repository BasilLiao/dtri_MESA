package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.SystemConfig;

public interface SystemConfigDao extends JpaRepository<SystemConfig, Long> {

	// 查詢全部
	ArrayList<SystemConfig> findAll();

	// 查詢一部分
	@Query("SELECT c FROM SystemConfig c "
			+ "WHERE (:scname is null or c.scname LIKE %:scname% ) and (:scgname is null or c.scgname LIKE %:scgname% ) and ( c.sysstatus = :sysstatus )  "
			+ "order by c.scgid desc,c.sysmdate desc")
	ArrayList<SystemConfig> findAllByConfig(@Param("scname") String scname, @Param("scgname") String scgname, @Param("sysstatus") Integer sysstatus,
			Pageable pageable);

	// 查詢是否重複 群組
	@Query("SELECT c FROM SystemConfig c " + "WHERE  (c.scgname = :scgname) " + "order by c.scgid desc")
	ArrayList<SystemConfig> findAllByConfigGroupTop1(@Param("scgname") String scgname, Pageable pageable);

	// 取得G_ID
	@Query(value = "SELECT NEXTVAL('system_config_g_seq')", nativeQuery = true)
	Long getSystem_config_g_seq();

	// 取得ID
	@Query(value = "SELECT CURRVAL('system_config_seq')", nativeQuery = true)
	Long getSystem_config_seq();

	// delete
	Long deleteByScidAndSysheader(Long id, Boolean sysheader);
}
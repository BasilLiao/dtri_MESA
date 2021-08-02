package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.ProductionSN;

public interface ProductionSNDao extends JpaRepository<ProductionSN, Long> {

	// 查詢全部
	ArrayList<ProductionSN> findAll();

	// 查詢全部
	ArrayList<ProductionSN> findAllByPsid(Long id);

	// 查詢一部分
	@Query("SELECT c FROM ProductionSN c " //
			+ "WHERE (:psname is null or c.psname LIKE %:psname% ) and "//
			+ "(:psgname is null or c.psgname LIKE %:psgname% ) and " //
			+ "( c.sysstatus = :sysstatus )  " //
			+ "order by c.psgid asc,c.sysheader desc,c.psid asc")
	ArrayList<ProductionSN> findAllByProductionSN(@Param("psname") String psname, @Param("psgname") String psgname,
			@Param("sysstatus") Integer sysstatus, Pageable pageable);

	// 查詢不含群組代表
	ArrayList<ProductionSN> findAllBySysheaderOrderByPsgidAsc(boolean sysheader);

	// 查詢是否重複 群組
	ArrayList<ProductionSN> findAllBySysheaderAndPsgid(boolean sysheader, Long psgid);

	// 取得最新G_ID
	@Query(value = "SELECT CURRVAL('production_g_sn_seq')", nativeQuery = true)
	Long getProduction_g_sn_seq();

	// 取得ID
	@Query(value = "SELECT NEXTVAL('production_sn_seq')", nativeQuery = true)
	Long getProduction_sn_seq();

	// delete
	Long deleteByPsidAndSysheader(Long id, Boolean sysheader);
}
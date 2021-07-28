package dtri.com.tw.db.pgsql.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.ProductionBody;

public interface ProductionBodyDao extends JpaRepository<ProductionBody, Long> {
	// 查詢 標籤
	List<ProductionBody> findAllByPbid(Integer id);

	// 取得下當前筆ID
	@Query(value = "SELECT CURRVAL('production_body_seq')", nativeQuery = true)
	Integer getProductionBodySeq();

	// 取得G_ID && 累加
	@Query(value = "SELECT NEXTVAL('production_body_g_seq')", nativeQuery = true)
	Integer getProductionBodyGSeq();

	// 查詢SN重複
	List<ProductionBody> findAllByPbsn(String pbsn);

	// 查詢SN重複+群組
	List<ProductionBody> findAllByPbsnAndPbgid(String pbsn, Integer pbgid);

	// 查詢SN群組
	List<ProductionBody> findAllByPbgidOrderByPbsnAsc(Integer pbgid);
	

	// 查詢一部分_Body
	@Query(value = "SELECT b FROM ProductionBody b WHERE "//
			+ "( b.sysstatus = :sysstatus ) and "//
			+ "(coalesce(:pbid, null) is null or b.pbid IN :pbid ) and "// coalesce 回傳非NULL值
			+ "(b.pbid!=0 or b.pbid!=1) and (b.sysheader!=true) "//
			+ " order by b.pbgid desc,b.pbid asc, b.sysmdate desc ")
	List<ProductionBody> findAllByProductionBody(@Param("sysstatus") Integer sys_status, @Param("pbid") List<Integer> pb_id,Pageable pageable);

	// 查詢一部分_Body By Check
	@Query(value = "SELECT b FROM ProductionBody b WHERE "//
			+ "( b.sysstatus = :sysstatus ) and "//
			+ "(coalesce(:pbid, null) is null or b.pbid IN :pbid ) and "// coalesce 回傳非NULL值
			+ "(b.pbid!=0 or b.pbid!=1) and (b.sysheader!=true) and  (b.pbcheck=:pbcheck)"//
			+ " order by b.pbgid desc,b.pbid asc ,b.sysheader desc")
	List<ProductionBody> findAllByProductionBody(@Param("sysstatus") Integer sys_status, @Param("pbid") List<Integer> pb_id,
			@Param("pbcheck") Boolean pb_check,Pageable pageable);

	// 移除單一SN
	Long deleteByPbid(Integer id);
	
	//移除 群組
	Long deleteByPbgid(Integer id);

}
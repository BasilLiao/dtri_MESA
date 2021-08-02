package dtri.com.tw.db.pgsql.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dtri.com.tw.db.entity.ProductionHeader;
import dtri.com.tw.db.entity.ProductionRecords;

@Repository
public interface ProductionHeaderDao extends JpaRepository<ProductionHeader, Long> {

	// 查詢全部
	List<ProductionHeader> findAll();

	// 查詢最新一筆的製令單關聯
	ProductionHeader findTopByPhpbgidOrderBySysmdateDesc(Long phpbgid);

//
//	// 查詢一部分_Header+Body
//	@Query("SELECT h FROM ProductionHeader h WHERE "//
//			+ "(:phmodel is null or h.phmodel LIKE %:phmodel% ) and "//
//			+ "(:phprid is null or h.phprid LIKE %:phprid% ) and "//
//			+ "( h.sysstatus = :sysstatus ) and "//
//			+ "(coalesce(:pbphid, null) is null or h.phid IN :pbphid ) and "//coalesce 回傳非NULL值
//			+ "(h.phid != 0) "
//			+ " order by h.sysmdate desc ")
//	List<ProductionHeader> findAllByProductionHeaderAndProductionBody(@Param("phmodel") String phmodel, @Param("phprid") String phprid,
//			@Param("sysstatus") Integer sysstatus,@Param("pbphid")List<Integer> pbphid, Pageable pageable);
//	
	// 查詢一部分_ProductionHeader+ProductionRecords
	@Query("SELECT h FROM ProductionHeader h join h.productionRecords r  WHERE "//
			+ "(:prpmodel is null or r.prpmodel LIKE %:prpmodel% ) and "//
			+ "(:phprid is null or r.prid LIKE %:phprid% ) and "//
			+ "(:prorderid is null or r.prorderid LIKE %:prorderid% ) and "//
			+ "(:prcname is null or r.prcname LIKE %:prcname% ) and "//
			+ "(:prbomid is null or r.prbomid LIKE %:prbomid% ) and "//
			+ "(:prbitem is null or r.prbitem LIKE %:prbitem% ) and "//
			+ "(:prsitem is null or r.prsitem LIKE %:prsitem% ) and "//
			+ "(:sysstatus = 0 or  h.sysstatus = :sysstatus ) and "//
			+ "(coalesce(:phpbgid, null) is null or h.phpbgid IN :phpbgid ) and "// coalesce 回傳非NULL值
			+ "(h.phid != 0) "//
			+ " order by h.sysmdate desc ")
	List<ProductionHeader> findAllByProductionHeader(@Param("prpmodel") String prpmodel, @Param("phprid") String phprid,
			@Param("sysstatus") Integer sysstatus, @Param("phpbgid") List<Long> phpbgid, @Param("prorderid") String pr_order_id,
			@Param("prcname") String pr_c_name, @Param("prbomid") String pr_bom_id, @Param("prbitem") String pr_b_item,
			@Param("prsitem") String pr_s_item, Pageable pageable);

	// 取得當筆ID
	@Query(value = "SELECT CURRVAL('production_header_seq')", nativeQuery = true)
	Long getProductionHeaderSeq();

	// 查詢重複製令
	List<ProductionHeader> findAllByProductionRecords(ProductionRecords phprid);

	// 查詢重複製令+狀態
	List<ProductionHeader> findAllByProductionRecordsAndSysstatusNotIn(ProductionRecords phprid, List<Integer> sysstatus);

	// 查詢重複製令+ID
	List<ProductionHeader> findAllByProductionRecordsAndPhwpid(ProductionRecords phprid, Long phwpid);

	// 查詢ID
	List<ProductionHeader> findAllByPhid(Long phid);

	// delete(header)
	Long deleteByPhidAndSysheader(Long id, Boolean sysheader);
}
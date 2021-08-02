package dtri.com.tw.db.pgsql.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dtri.com.tw.db.entity.ProductionRecords;
import dtri.com.tw.db.entity.WorkHours;
import dtri.com.tw.db.entity.WorkType;

public interface WorkHoursDao extends JpaRepository<WorkHours, Long> {

	// 查詢ID
	List<WorkHours> findByWhidOrderByWhidAsc(Integer whid);

	// 查詢工時名稱->查詢群組區間
	@Query("SELECT c FROM WorkHours c " //
			+ "WHERE "
			// + "(:whaccount is null or c.whaccount LIKE %:whaccount%) and "//
			+ "(:whprid is null or c.productionRecords.prid LIKE %:whprid%) and "//
			+ "(:whdo is null or c.whdo LIKE %:whdo%) and "//
			+ "(:whwtid = 0 or c.workType.wtid = :whwtid) and "//
			+ "(cast(:whsdate as date) is null or :whsdate <= c.whsdate) and "//
			+ "(cast(:whedate as date) is null or c.whedate <= :whedate) and "//
			+ "(c.sysheader = false ) and "//
			+ "( :sysstatus = 0 or c.sysstatus = :sysstatus ) "//
			+ "order by c.productionRecords.prid asc,c.sysheader desc,c.workType.wtid desc, c.whsdate asc")
	List<WorkHours> findAllByWorkHours(String whprid, /* String whaccount, */ String whdo, Integer whwtid, Integer sysstatus, Date whsdate,
			Date whedate, Pageable p);

	// 群組清單
	@Query("SELECT c FROM WorkHours c "//
			+ "WHERE  ((:prid) is null or c.productionRecords.prid in (:prid)) "//
			+ "order by c.productionRecords asc,c.sysheader desc,c.workType.wtid desc, c.whsdate asc")
	List<WorkHours> findAllByWorkHours(List<String> prid);

	List<WorkHours> findAllByWhid(Long id);

	// 查詢群
	List<WorkHours> findAllByproductionRecords(ProductionRecords wh_pr_id);

	// 查詢群
	List<WorkHours> findAllByproductionRecordsAndWorkTypeAndSysstatus(ProductionRecords wh_pr_id, WorkType wt_id, Integer sysstatus);

	// 移除
	Long deleteByWhid(Long whid);

	// 移除(群組)
	Long deleteByproductionRecords(ProductionRecords wh_pr_id);

}
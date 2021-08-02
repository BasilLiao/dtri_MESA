package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.WorkType;

public interface WorkTypeDao extends JpaRepository<WorkType, Long> {

	// 查詢全部
	ArrayList<WorkType> findAll();

	// 查詢一部分
	@Query("SELECT c FROM WorkType c "
			+ "WHERE (:wtname is null or c.wtname LIKE %:wtname% )  and ( c.sysstatus = :sysstatus ) and wtid !=0  "
			+ "order by c.wtname desc,c.sysmdate desc")
	ArrayList<WorkType> findAllByWorkType(@Param("wtname") String wtname, @Param("sysstatus") Integer sysstatus,
			Pageable pageable);

	// delete
	Long deleteByWtid(Long id);
}
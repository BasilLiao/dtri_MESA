package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dtri.com.tw.db.entity.SystemGroup;

public interface SystemGroupDao extends JpaRepository<SystemGroup, Long> {

	// 查詢群組
	@Query("SELECT c FROM SystemGroup c "
			+ "WHERE  (c.sggid = :sggid) "
			+ "order by c.sggid asc, c.systemPermission.syssort asc")
	List<SystemGroup> findBySggidOrderBySggid(Long sggid);

	// 查詢ID
	List<SystemGroup> findBySgidOrderBySgidAscSyssortAsc(Long sgid);

	// 查詢群組名稱
	@Query("SELECT c FROM SystemGroup c "
			+ "WHERE  (:sgname is null or c.sgname LIKE %:sgname%) and ( c.sysstatus = :sysstatus ) and ((:sggid) is null or c.sggid in (:sggid)) "
			+ "order by c.sggid asc, c.sgid asc")
	List<SystemGroup> findAllBySystemGroup(String sgname, Integer sysstatus, List<Long> sggid);

	// 查詢群組[頭]數量
	List<SystemGroup> findAllBySysheader(boolean sysheader, Pageable p);
	
	// 查詢群組[頭]數量不包含 Admin群組
		List<SystemGroup> findAllBySysheaderAndSgidNot(boolean sysheader,Long sgid, Pageable p);

	// 查詢群組分頁
	List<SystemGroup> findAllByOrderBySggidAscSgidAsc(Pageable p);

	// 查詢全部
	ArrayList<SystemGroup> findAll();

	// 查詢是否重複 群組
	@Query("SELECT c FROM SystemGroup c " + "WHERE  (c.sgname = :sgname) " + "order by c.sgid desc")
	ArrayList<SystemGroup> findAllByGroupTop1(@Param("sgname") String spgname, Pageable pageable);

	// 取得G_ID
	@Query(value = "SELECT NEXTVAL('system_group_g_seq')", nativeQuery = true)
	Long getSystem_group_g_seq();

	// 移除
	Long deleteBySggid(Long sggid);

}
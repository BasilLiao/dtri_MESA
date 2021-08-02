package dtri.com.tw.db.pgsql.dao;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dtri.com.tw.db.entity.SystemUser;

public interface SystemUserDao extends JpaRepository<SystemUser, Long> {

	// 帳號查詢
	SystemUser findBySuaccount(String suaccount);

	// 查詢全部
	ArrayList<SystemUser> findAll();

	// 查詢ID
	ArrayList<SystemUser> findAllBySuid(Long id);

	// 查詢全部含-頁數
	@Query("SELECT c FROM SystemUser c "//
			+ "WHERE (:suname is null or c.suname LIKE %:suname% ) and "//
			+ "(:suaccount is null or c.suaccount LIKE %:suaccount% ) and "//
			+ "( c.sysstatus = :sysstatus )  "//
			+ "order by c.sysmdate desc")
	ArrayList<SystemUser> findAllBySystemUser(String suname, String suaccount, Integer sysstatus, Pageable pageable);

	// 查詢全部含-頁數 不含ADMIN
	@Query("SELECT c FROM SystemUser c "//
			+ "WHERE (:suname is null or c.suname LIKE %:suname% ) and "//
			+ "(:suaccount is null or c.suaccount LIKE %:suaccount% ) and "//
			+ "( c.sysstatus = :sysstatus ) and (c.susggid != 1) "//
			+ "order by c.sysmdate desc")
	ArrayList<SystemUser> findAllBySystemUserNotAdmin(String suname, String suaccount, Integer sysstatus, Pageable pageable);
	
	// @Query註解裡面寫JPQL語句,定義查詢
	@Query(nativeQuery = false, value = " SELECT i FROM SystemUser i WHERE su_id = ?1")
	SystemUser readId(Long id);

	Long deleteBySuid(Long suid);

}
package dtri.com.tw.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Basil
 * @see 系統設定<br>
 *      wh_id : ID<br>
 *      wh_pr_id : 製令單ID(1對多)<br>
 *      wh_wt_id : 工作類型<br>
 *      wh_account : 作業人(承包人)<br>
 *      wh_do : 工作內容<br>
 *      wh_nb: 製作數量<br>
 *      wh_s_date:開始時間<br>
 *      wh_e_date:結束時間<br>
 */
@Entity
@Table(name = "work_hours")
@EntityListeners(AuditingEntityListener.class)
public class WorkHours {
	public WorkHours() {
		this.syscdate = new Date();
		this.syscuser = "system";
		this.sysmdate = new Date();
		this.sysmuser = "system";
		this.sysver = 0;
		this.sysnote = "";
		this.syssort = 0;
		this.sysstatus = 0;
		this.sysheader = false;
	}

	// 共用型
	@Column(name = "sys_c_date", nullable = false, columnDefinition = "TIMESTAMP default now()")
	private Date syscdate;

	@Column(name = "sys_c_user", nullable = false, columnDefinition = "varchar(50) default 'system'")
	private String syscuser;

	@Column(name = "sys_m_date", nullable = false, columnDefinition = "TIMESTAMP default now()")
	private Date sysmdate;

	@Column(name = "sys_m_user", nullable = false, columnDefinition = "varchar(50) default 'system'")
	private String sysmuser;

	@Column(name = "sys_ver", columnDefinition = "int default 0")
	private Integer sysver;

	@Column(name = "sys_note", columnDefinition = "text default ''")
	private String sysnote;

	@Column(name = "sys_sort", columnDefinition = "int default 0")
	private Integer syssort;

	@Column(name = "sys_status", columnDefinition = "int default 0")
	private Integer sysstatus;

	@Column(name = "sys_header", nullable = false, columnDefinition = "boolean default false")
	private Boolean sysheader;

	// 功能項目
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_hours_seq")
	@SequenceGenerator(name = "work_hours_seq", sequenceName = "work_hours_seq", allocationSize = 1)
	@Column(name = "wh_id")
	private Long whid;

	@ManyToOne(targetEntity = ProductionRecords.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "wh_pr_id")
	private ProductionRecords productionRecords;

	@ManyToOne(targetEntity = WorkType.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "wh_wt_id")
	private WorkType workType;

	@Column(name = "wh_account", nullable = false, columnDefinition = "varchar(50)")
	private String whaccount;

	@Column(name = "wh_do", nullable = false, columnDefinition = "varchar(100)")
	private String whdo;

	@Column(name = "wh_nb", nullable = false)
	private Integer whnb;

	@Column(name = "wh_s_date", columnDefinition = "TIMESTAMP")
	private Date whsdate;

	@Column(name = "wh_e_date", columnDefinition = "TIMESTAMP")
	private Date whedate;

	
	public Date getSyscdate() {
		return syscdate;
	}

	public void setSyscdate(Date syscdate) {
		this.syscdate = syscdate;
	}

	public String getSyscuser() {
		return syscuser;
	}

	public void setSyscuser(String syscuser) {
		this.syscuser = syscuser;
	}

	public Date getSysmdate() {
		return sysmdate;
	}

	public void setSysmdate(Date sysmdate) {
		this.sysmdate = sysmdate;
	}

	public String getSysmuser() {
		return sysmuser;
	}

	public void setSysmuser(String sysmuser) {
		this.sysmuser = sysmuser;
	}

	public Integer getSysver() {
		return sysver;
	}

	public void setSysver(Integer sysver) {
		this.sysver = sysver;
	}

	public String getSysnote() {
		return sysnote;
	}

	public void setSysnote(String sysnote) {
		this.sysnote = sysnote;
	}

	public Integer getSyssort() {
		return syssort;
	}

	public void setSyssort(Integer syssort) {
		this.syssort = syssort;
	}

	public Integer getSysstatus() {
		return sysstatus;
	}

	public void setSysstatus(Integer sysstatus) {
		this.sysstatus = sysstatus;
	}

	public Boolean getSysheader() {
		return sysheader;
	}

	public void setSysheader(Boolean sysheader) {
		this.sysheader = sysheader;
	}

	public Long getWhid() {
		return whid;
	}

	public void setWhid(Long whid) {
		this.whid = whid;
	}



	public WorkType getWhwtid() {
		return workType;
	}

	public void setWhwtid(WorkType workType) {
		this.workType = workType;
	}

	public String getWhaccount() {
		return whaccount;
	}

	public void setWhaccount(String whaccount) {
		this.whaccount = whaccount;
	}

	public String getWhdo() {
		return whdo;
	}

	public void setWhdo(String whdo) {
		this.whdo = whdo;
	}

	public Integer getWhnb() {
		return whnb;
	}

	public void setWhnb(Integer whnb) {
		this.whnb = whnb;
	}

	public Date getWhsdate() {
		return whsdate;
	}

	public void setWhsdate(Date whsdate) {
		this.whsdate = whsdate;
	}

	public Date getWhedate() {
		return whedate;
	}

	public void setWhedate(Date whedate) {
		this.whedate = whedate;
	}

	public ProductionRecords getProductionRecords() {
		return productionRecords;
	}

	public void setProductionRecords(ProductionRecords productionRecords) {
		this.productionRecords = productionRecords;
	}

}

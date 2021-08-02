package dtri.com.tw.db.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Basil
 * @see 產品製程<br>
 *      ph_id : ID<br>
 *      ph_pr_id : 關聯-製令工單<br>
 *      ph_pr_type : 類型-製令工單<br>
 *      ph_pb_id : 關聯-SN清單<br>
 *      ph_pb_g_id :關聯群組-SN清單<br>
 *      ph_p_name:(客戶)產品名稱/產品批次驗證序號(product name/part name)
 *      ph_wpro_id : 工作站<br>
 *      ph_s_date : 開始製成 <br>
 *      ph_e_date : 結束製成 <br>
 *      ph_schedule : 進度{A站:{A1項目:Y,A2項目:N},{B站:{...},...}}<br>
 * 
 */
@Entity
@Table(name = "production_header")
@EntityListeners(AuditingEntityListener.class)
public class ProductionHeader {
	public ProductionHeader() {
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "production_header_seq")
	@SequenceGenerator(name = "production_header_seq", sequenceName = "production_header_seq", allocationSize = 1)
	@Column(name = "ph_id")
	private Long phid;

	@Column(name = "ph_pb_g_id")
	private Long phpbgid;

	// @Column(name = "ph_pr_id", nullable = false, columnDefinition =
	// "varchar(50)")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ph_pr_id", referencedColumnName = "pr_id")
	private ProductionRecords productionRecords;

	@Column(name = "ph_type", columnDefinition = "varchar(50)")
	private String phtype;
	
	@Column(name = "ph_p_number", columnDefinition = "varchar(50)")
	private String phpnumber;
	
	@Column(name = "ph_p_name", columnDefinition = "varchar(50)")
	private String phpname;

	@Column(name = "ph_wp_id", nullable = false)
	private Long phwpid;

	@Column(name = "ph_s_date", columnDefinition = "TIMESTAMP")
	private Date phsdate;

	@Column(name = "ph_e_date", columnDefinition = "TIMESTAMP")
	private Date phedate;

	@Column(name = "ph_schedule", columnDefinition = "varchar(50)")
	private String phschedule;

	public String getPhpnumber() {
		return phpnumber;
	}

	public void setPhpnumber(String phpnumber) {
		this.phpnumber = phpnumber;
	}

	public Long getPhpbgid() {
		return phpbgid;
	}

	public void setPhpbgid(Long phpbgid) {
		this.phpbgid = phpbgid;
	}

	public String getPhtype() {
		return phtype;
	}

	public void setPhtype(String phtype) {
		this.phtype = phtype;
	}

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

	public Long getPhid() {
		return phid;
	}

	public void setPhid(Long phid) {
		this.phid = phid;
	}
	

	public ProductionRecords getProductionRecords() {
		return productionRecords;
	}

	public void setProductionRecords(ProductionRecords productionRecords) {
		this.productionRecords = productionRecords;
	}

	public Long getPhwpid() {
		return phwpid;
	}

	public void setPhwpid(Long phwpid) {
		this.phwpid = phwpid;
	}

	public Date getPhsdate() {
		return phsdate;
	}

	public void setPhsdate(Date phsdate) {
		this.phsdate = phsdate;
	}

	public Date getPhedate() {
		return phedate;
	}

	public void setPhedate(Date phedate) {
		this.phedate = phedate;
	}

	public String getPhschedule() {
		return phschedule;
	}

	public void setPhschedule(String phschedule) {
		this.phschedule = phschedule;
	}

	public String getPhpname() {
		return phpname;
	}

	public void setPhpname(String phpname) {
		this.phpname = phpname;
	}

}

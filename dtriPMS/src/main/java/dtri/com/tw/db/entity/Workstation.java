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
 * 
 * @see ---共用型---<br>
 *      sys_c_date : 創建時間<br>
 *      sys_c_user : 創建人名<br>
 *      sys_m_date : 修改時間<br>
 *      sys_m_user : 修改人名<br>
 *      sys_ver : 修改版本<br>
 *      sys_note : 備註<br>
 *      sys_status : 資料狀態<br>
 *      sys_sort : 自訂排序<br>
 *      ---工作站項目---<br>
 *      w_id : 主key<br>
 *      w_g_id : 群組<br>
 *      w_i_id : 項目ID<br>
 *      w_codename : 登記工作站條碼用<br>
 *      w_pb_name : 工作站名稱(來自於pb_w_name)<br>
 *      w_pb_cell : 工作站名稱(來自於pb 欄位名稱)<br>
 *      w_sg_id : 可使用此工作站群組[ID]<br>
 *      w_sg_name : 可使用此工作站群組[名稱]<br>
 *      w_replace : 是否可重複刷入資料
 * 
 * 
 * 
 **/
@Entity
@Table(name = "workstation")
@EntityListeners(AuditingEntityListener.class)
public class Workstation {

	public Workstation() {
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

	@Column(name = "sys_status", columnDefinition = "int default 0")
	private Integer sysstatus;

	@Column(name = "sys_sort", columnDefinition = "int default 0")
	private Integer syssort;

	@Column(name = "sys_header", nullable = false, columnDefinition = "boolean default false")
	private Boolean sysheader;

	// 工作站
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "workstation_seq")
	@SequenceGenerator(name = "workstation_seq", sequenceName = "workstation_seq", allocationSize = 1)
	@Column(name = "w_id")
	private Long wid;

	@Column(name = "w_g_id", nullable = false)
	private Long wgid;

	@ManyToOne(targetEntity = WorkstationItem.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "w_i_id")
	private WorkstationItem workstationItem;

	@Column(name = "w_c_name", nullable = false, columnDefinition = "varchar(50)")
	private String wcname;

	@Column(name = "w_pb_name", nullable = false, columnDefinition = "varchar(50)")
	private String wpbname;

	@Column(name = "w_pb_cell", nullable = false, columnDefinition = "varchar(50)")
	private String wpbcell;

	@Column(name = "w_sg_id", columnDefinition = "int default 0")
	private Long wsgid;

	@Column(name = "w_sg_name", columnDefinition = "varchar(50) default ''")
	private String wsgname;
	
	@Column(name = "w_replace", columnDefinition = "boolean default true")
	private Boolean wreplace;	
	

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

	public Integer getSysstatus() {
		return sysstatus;
	}

	public void setSysstatus(Integer sysstatus) {
		this.sysstatus = sysstatus;
	}

	public Integer getSyssort() {
		return syssort;
	}

	public void setSyssort(Integer syssort) {
		this.syssort = syssort;
	}

	public Boolean getSysheader() {
		return sysheader;
	}

	public void setSysheader(Boolean sysheader) {
		this.sysheader = sysheader;
	}

	public Long getWid() {
		return wid;
	}

	public void setWid(Long wid) {
		this.wid = wid;
	}

	public Long getWgid() {
		return wgid;
	}

	public void setWgid(Long wgid) {
		this.wgid = wgid;
	}

	public WorkstationItem getWorkstationItem() {
		return workstationItem;
	}

	public void setWorkstationItem(WorkstationItem workstationItem) {
		this.workstationItem = workstationItem;
	}

	public String getWcname() {
		return wcname;
	}

	public void setWcname(String wcname) {
		this.wcname = wcname;
	}

	public String getWpbname() {
		return wpbname;
	}

	public void setWpbname(String wpbname) {
		this.wpbname = wpbname;
	}

	public String getWpbcell() {
		return wpbcell;
	}

	public void setWpbcell(String wpbcell) {
		this.wpbcell = wpbcell;
	}

	public Long getWsgid() {
		return wsgid;
	}

	public void setWsgid(Long wsgid) {
		this.wsgid = wsgid;
	}

	public String getWsgname() {
		return wsgname;
	}

	public void setWsgname(String wsgname) {
		this.wsgname = wsgname;
	}

	public Boolean getWreplace() {
		return wreplace;
	}

	public void setWreplace(Boolean wreplace) {
		this.wreplace = wreplace;
	}
	
}

package dtri.com.tw.db.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Basil
 * @see 工作站項目<br>
 *      wi_id : ID<br>
 *      wi_pb_cell : pb的欄位名稱<br>
 *      wi_pb_value :pb的<br>
 */
@Entity
@Table(name = "workstation_Item")
@EntityListeners(AuditingEntityListener.class)
public class WorkstationItem {
	public WorkstationItem() {
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workstation_item_seq")
	@SequenceGenerator(name = "workstation_item_seq", sequenceName = "workstation_item_seq", allocationSize = 1)
	@Column(name = "wi_id")
	private Long wiid;

	@Column(name = "wi_pb_cell", nullable = false, columnDefinition = "varchar(50)")
	private String wipbcell;

	@Column(name = "wi_pb_value", nullable = false, columnDefinition = "varchar(50)")
	private String wipbvalue;

	@OneToMany(mappedBy = "workstationItem")
	private List<Workstation> workstation;

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

	public Long getWiid() {
		return wiid;
	}

	public void setWiid(Long wiid) {
		this.wiid = wiid;
	}

	public String getWipbcell() {
		return wipbcell;
	}

	public void setWipbcell(String wipbcell) {
		this.wipbcell = wipbcell;
	}

	public String getWipbvalue() {
		return wipbvalue;
	}

	public void setWipbvalue(String wipbvalue) {
		this.wipbvalue = wipbvalue;
	}

	public List<Workstation> getWorkstation() {
		return workstation;
	}

	public void setWorkstation(List<Workstation> workstation) {
		this.workstation = workstation;
	}

	
}

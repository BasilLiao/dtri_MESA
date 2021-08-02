package dtri.com.tw.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 *      wp_id : 主key<br>
 *      wp_g_id : 群組<br>
 *      wp_name : 工作程序名稱<br>
 *      wp_w_g_id : 工作站(群組)<br>
 *      wp_c_name :2維代號(選擇工作流程)<br>
 * 
 * 
 **/
@Entity
@Table(name = "workstation_program")
@EntityListeners(AuditingEntityListener.class)
public class WorkstationProgram {

	public WorkstationProgram() {
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
	@Column(name = "wp_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "workstation_program_seq")
	@SequenceGenerator(name = "workstation_program_seq", sequenceName = "workstation_program_seq", allocationSize = 1)
	private Long wpid;

	@Column(name = "wp_g_id", nullable = false)
	private Long wpgid;

	@Column(name = "wp_name", nullable = false, columnDefinition = "varchar(50)")
	private String wpname;

	@Column(name = "wp_w_g_id", nullable = false)
	private Long wpwgid;

	@Column(name = "wp_c_name", nullable = false, columnDefinition = "varchar(50)")
	private String wpcname;

	public Boolean getSysheader() {
		return sysheader;
	}

	public void setSysheader(Boolean sysgheader) {
		this.sysheader = sysgheader;
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

	public Long getWpid() {
		return wpid;
	}

	public void setWpid(Long wpid) {
		this.wpid = wpid;
	}

	public Long getWpgid() {
		return wpgid;
	}

	public void setWpgid(Long wpgid) {
		this.wpgid = wpgid;
	}

	public String getWpname() {
		return wpname;
	}

	public void setWpname(String wpname) {
		this.wpname = wpname;
	}

	public Long getWpwgid() {
		return wpwgid;
	}

	public void setWpwgid(Long wpwgid) {
		this.wpwgid = wpwgid;
	}

	public String getWpcname() {
		return wpcname;
	}

	public void setWpcname(String wpcname) {
		this.wpcname = wpcname;
	}
}

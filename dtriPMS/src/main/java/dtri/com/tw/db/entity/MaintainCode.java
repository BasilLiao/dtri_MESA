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
 * @see 系統設定<br>
 *      mc_id : ID<br>
 *      mc_name : 名稱<br>
 *      mc_g_id : 群組ID<br>
 *      mc_g_name : 群組名稱<br>
 *      mc_value : 設定參數<br>
 */
@Entity
@Table(name = "maintain_code")
@EntityListeners(AuditingEntityListener.class)
public class MaintainCode {
	public MaintainCode() {
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "maintain_code_seq")
	@SequenceGenerator(name = "maintain_code_seq", sequenceName = "maintain_code_seq", allocationSize = 1)
	@Column(name = "mc_id")
	private Long mcid;

	@Column(name = "mc_g_id", nullable = false)
	private Long mcgid;

	@Column(name = "mc_name", nullable = false, columnDefinition = "varchar(50)")
	private String mcname;

	@Column(name = "mc_g_name", nullable = false, columnDefinition = "varchar(50)")
	private String mcgname;

	@Column(name = "mc_value", nullable = false, columnDefinition = "varchar(50)")
	private String mcvalue;

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

	public Long getMcid() {
		return mcid;
	}

	public void setMcid(Long mcid) {
		this.mcid = mcid;
	}

	public Long getMcgid() {
		return mcgid;
	}

	public void setMcgid(Long mcgid) {
		this.mcgid = mcgid;
	}

	public String getMcname() {
		return mcname;
	}

	public void setMcname(String mcname) {
		this.mcname = mcname;
	}

	public String getMcgname() {
		return mcgname;
	}

	public void setMcgname(String mcgname) {
		this.mcgname = mcgname;
	}

	public String getMcvalue() {
		return mcvalue;
	}

	public void setMcvalue(String mcvalue) {
		this.mcvalue = mcvalue;
	}

}

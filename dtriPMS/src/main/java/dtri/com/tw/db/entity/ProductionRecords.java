package dtri.com.tw.db.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Basil
 * @see 系統設定<br>
 *      pr_id : 工單序號ID<br>
 *      pr_order_id : 訂單編號<br>
 *      pr_c_name : 客戶名稱<br>
 *      pr_p_quantity : 生產數量<br>
 *      pr_p_model : 產品型號<br>
 *      pr_bom_id : BOM料號<br>
 *      pr_c_from : 單據來源<br>
 *      pr_b_item : 規格定義{"名稱1":"內容1","名稱2":"內容2"}<br>
 *      pr_s_item : 軟體定義{"名稱1":"內容1","名稱2":"內容2"}<br>
 *      pr_s_sn : 產品序號 開始 EX:xxxxxx 01YW12042J044-<br>
 *      pr_e_sn : 產品序號 結束 EX: xxxxxx 01YW12042J050<br>
 *      pr_s_b_sn : 燒錄序號 開始 EX:xxxxxx 01YW12042J044-<br>
 *      pr_e_b_sn : 燒錄序號 結束 EX:xxxxxx 01YW12042J044-<br>
 *      pr_w_years : 保固
 * 
 */
@Entity
@Table(name = "production_records")
@EntityListeners(AuditingEntityListener.class)
public class ProductionRecords {
	public ProductionRecords() {
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
	// 因為是文字 故無用 自動累加
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "production_records_seq")
	// @SequenceGenerator(name = "production_records_seq", sequenceName =
	// "production_records_seq", allocationSize = 1)
	@Column(name = "pr_id")
	private String prid;

	@Column(name = "pr_order_id", nullable = false, columnDefinition = "varchar(60) default ''")
	private String prorderid;

	@Column(name = "pr_c_name", nullable = false, columnDefinition = "varchar(60) default ''")
	private String prcname;

	@Column(name = "pr_p_quantity", nullable = false, columnDefinition = "int default 0")
	private Integer prpquantity;

	@Column(name = "pr_p_ok_quantity", columnDefinition = "int default 0")
	private Integer prpokquantity;

	@Column(name = "pr_h_ok_quantity", columnDefinition = "int default 0")
	private Integer prhokquantity;

	@Column(name = "pr_p_model", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prpmodel;

	@Column(name = "pr_bom_id", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prbomid;

	// @Column(name = "pr_v_motherboard", nullable = false, columnDefinition =
	// "varchar(50) default ''")
	// private String prvmotherboard;

	@Column(name = "pr_c_from", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prcfrom;

	@Column(name = "pr_b_item", nullable = false, columnDefinition = "text default ''")
	private String prbitem;

	@Column(name = "pr_s_item", nullable = false, columnDefinition = "text default ''")
	private String prsitem;

	@Column(name = "pr_s_sn", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prssn;

	@Column(name = "pr_e_sn", nullable = false, columnDefinition = "varchar(50) default ''")
	private String presn;
	
	@Column(name = "pr_s_b_sn", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prsbsn;

	@Column(name = "pr_e_b_sn", nullable = false, columnDefinition = "varchar(50) default ''")
	private String prebsn;	

	@Column(name = "pr_w_years", columnDefinition = "int default 0")
	private Integer prwyears;

	@OneToOne(mappedBy = "productionRecords")
	private ProductionHeader header;

	@OneToMany(mappedBy = "productionRecords")
	private List<WorkHours> workHours;

	public Integer getPrpokquantity() {
		return prpokquantity;
	}

	public void setPrpokquantity(Integer prpokquantity) {
		this.prpokquantity = prpokquantity;
	}

	public String getPrsbsn() {
		return prsbsn;
	}

	public void setPrsbsn(String prsbsn) {
		this.prsbsn = prsbsn;
	}

	public String getPrebsn() {
		return prebsn;
	}

	public void setPrebsn(String prebsn) {
		this.prebsn = prebsn;
	}

	public ProductionHeader getHeader() {
		return header;
	}

	public void setHeader(ProductionHeader header) {
		this.header = header;
	}

	public List<WorkHours> getWorkHours() {
		return workHours;
	}

	public void setWorkHours(List<WorkHours> workHours) {
		this.workHours = workHours;
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

	public String getPrid() {
		return prid;
	}

	public void setPrid(String prid) {
		this.prid = prid;
	}

	public String getProrderid() {
		return prorderid;
	}

	public void setProrderid(String prorderid) {
		this.prorderid = prorderid;
	}

	public String getPrcname() {
		return prcname;
	}

	public void setPrcname(String prcname) {
		this.prcname = prcname;
	}

	public Integer getPrpquantity() {
		return prpquantity;
	}

	public void setPrpquantity(Integer prpquantity) {
		this.prpquantity = prpquantity;
	}

	public String getPrpmodel() {
		return prpmodel;
	}

	public void setPrpmodel(String prpmodel) {
		this.prpmodel = prpmodel;
	}

	public String getPrbomid() {
		return prbomid;
	}

	public void setPrbomid(String prbomid) {
		this.prbomid = prbomid;
	}

	public String getPrcfrom() {
		return prcfrom;
	}

	public void setPrcfrom(String prcfrom) {
		this.prcfrom = prcfrom;
	}

	public String getPrbitem() {
		return prbitem;
	}

	public void setPrbitem(String prbitem) {
		this.prbitem = prbitem;
	}

	public String getPrsitem() {
		return prsitem;
	}

	public void setPrsitem(String prsitem) {
		this.prsitem = prsitem;
	}

	public String getPrssn() {
		return prssn;
	}

	public void setPrssn(String prssn) {
		this.prssn = prssn;
	}

	public String getPresn() {
		return presn;
	}

	public void setPresn(String presn) {
		this.presn = presn;
	}

	public Integer getPrwyears() {
		return prwyears;
	}

	public void setPrwyears(Integer prwyears) {
		this.prwyears = prwyears;
	}

	public Integer getPrhokquantity() {
		return prhokquantity;
	}

	public void setPrhokquantity(Integer prhokquantity) {
		this.prhokquantity = prhokquantity;
	}
}

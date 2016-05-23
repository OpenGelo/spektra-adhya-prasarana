package app.entities.kepegawaian.uang.prestasi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "data_motor")
public class Motor {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@Column(name = "no_polisi", length = 20)
	private String noPolisi;

	@Column(name = "merek_motor", nullable = false, length = 50)
	private String merkMotor;

	@Column(name = "tanggal_pesan", nullable = false)
	private Date tanggalPesan;

	@Column(name = "total_angsuran")
	private Integer totalAngsuran;

	@Column(name = "downpayment", nullable = false)
	private Double dp;

	@Column(name = "disetujui", nullable = false)
	private Boolean setuju;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "motor", orphanRemoval = true)
	private List<PembayaranCicilanMotor> daftarCicilan = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoPolisi() {
		return noPolisi;
	}

	public void setNoPolisi(String noPolisi) {
		this.noPolisi = noPolisi;
	}

	public Date getTanggalPesan() {
		return tanggalPesan;
	}

	public void setTanggalPesan(Date tanggalPesan) {
		this.tanggalPesan = tanggalPesan;
	}

	public Integer getTotalAngsuran() {
		return totalAngsuran;
	}

	public void setTotalAngsuran(Integer totalAngsuran) {
		this.totalAngsuran = totalAngsuran;
	}

	public Double getDp() {
		return dp;
	}

	public void setDp(Double dp) {
		this.dp = dp;
	}

}
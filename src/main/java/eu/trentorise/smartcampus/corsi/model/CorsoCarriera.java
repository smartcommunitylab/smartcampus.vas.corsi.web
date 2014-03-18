package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "CorsoCarriera.findCorsoCarrieraByStudenteId", query = "select cc from CorsoCarriera cc where cc.studenteId = ?1"),
	@NamedQuery(name = "CorsoCarriera.findCorsoCarrieraByAdCodAndStudenteId", query = "select cc from CorsoCarriera cc where cc.cod = ?1 and cc.studenteId = ?2")})
public class CorsoCarriera extends BasicEntity {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1306548062859361763L;

		@Id
		@Column(name = "ID")
		private long id;

		@Column(name = "COD")
		private String cod;
		
		@Column(name = "NAME")
		private String name;

		@Column(name = "RESULT")
		private String result;
		
		@Column(name = "LODE")
		private boolean lode;

		@Column(name = "weight")
		private String weight;
		
		@Column(name = "DATE")
		private Date date;
		
		@Column(name = "STUDENTE_ID")
		private long studenteId;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getCod() {
			return cod;
		}

		public void setCod(String cod) {
			this.cod = cod;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public boolean isLode() {
			return lode;
		}

		public void setLode(boolean lode) {
			this.lode = lode;
		}

		public String getWeight() {
			return weight;
		}

		public void setWeight(String weight) {
			this.weight = weight;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public long getStudente() {
			return studenteId;
		}

		public void setStudente(long studenteId) {
			this.studenteId = studenteId;
		}

		
		
	
}

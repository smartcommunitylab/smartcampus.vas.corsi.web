package eu.trentorise.smartcampus.corsi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CorsoCarriera extends BasicEntity {

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
		
		
		
	
}

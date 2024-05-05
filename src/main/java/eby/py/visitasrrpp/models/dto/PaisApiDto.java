package eby.py.visitasrrpp.models.dto;

public class PaisApiDto {

	private NamePais name;
	private String cca3;
	private IddPais idd;
	private Bandera flags;

	public NamePais getName() {
		return name;
	}

	public void setName(NamePais name) {
		this.name = name;
	}

	public String getCca3() {
		return cca3;
	}

	public void setCca3(String cca3) {
		this.cca3 = cca3;
	}

	public IddPais getIdd() {
		return idd;
	}

	public void setIdd(IddPais idd) {
		this.idd = idd;
	}

	

	public Bandera getFlags() {
		return flags;
	}

	public void setFlags(Bandera flags) {
		this.flags = flags;
	}

	@Override
	public String toString() {
		
		return "name: " + name.getCommon() + " "
				+ "cca2: " + cca3 + " "
				+ "idd root: " + idd.getRoot() + " "
				+ "idd sufixes: " + idd.getSuffixes(); 
	}
	
	

}

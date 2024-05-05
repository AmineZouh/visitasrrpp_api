package eby.py.visitasrrpp.models.dto;

public class NamePais {

	private String common;
	private String official;
	private NativeName nativeName;

	public String getCommon() {
		return common;
	}

	public void setCommon(String value) {
		this.common = value;
	}

	public String getOfficial() {
		return official;
	}

	public void setOfficial(String value) {
		this.official = value;
	}

	public NativeName getNativeName() {
		return nativeName;
	}

	public void setNativeName(NativeName value) {
		this.nativeName = value;
	}

}

class NativeName {
	private SPA spa;

	public SPA getSPA() {
		return spa;
	}

	public void setSPA(SPA value) {
		this.spa = value;
	}
}

class SPA {
	private String official;
	private String common;

	public String getOfficial() {
		return official;
	}

	public void setOfficial(String value) {
		this.official = value;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String value) {
		this.common = value;
	}
}
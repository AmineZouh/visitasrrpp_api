package eby.py.visitasrrpp.models.dto;

import eby.py.visitasrrpp.models.entity.TipoTransporte;

public class TransporteDto {

	private Long idTransporte;
	private String nroChapa;
	private TipoTransporte tipoTransporte;

	public Long getIdTransporte() {
		return idTransporte;
	}

	public void setIdTransporte(Long idTransporte) {
		this.idTransporte = idTransporte;
	}

	public String getNroChapa() {
		return nroChapa;
	}

	public void setNroChapa(String nroChapa) {
		this.nroChapa = nroChapa;
	}

	public TipoTransporte getTipoTransporte() {
		return tipoTransporte;
	}

	public void setTipoTransporte(TipoTransporte tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

}

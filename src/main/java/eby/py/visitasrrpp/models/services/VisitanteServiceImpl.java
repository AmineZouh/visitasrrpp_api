package eby.py.visitasrrpp.models.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import eby.py.visitasrrpp.models.dao.IImageSideDao;
import eby.py.visitasrrpp.models.dao.IPaisDao;
import eby.py.visitasrrpp.models.dao.ITipoDocumentoDao;
import eby.py.visitasrrpp.models.dao.IVisitanteDao;
import eby.py.visitasrrpp.models.dao.IVisitanteGroupDao;
import eby.py.visitasrrpp.models.dto.VisitanteDto;
import eby.py.visitasrrpp.models.dto.VisitanteSaveRequest;
import eby.py.visitasrrpp.models.dto.VisitanteWithGroupeSaveRequest;
import eby.py.visitasrrpp.models.entity.ImageSide;
import eby.py.visitasrrpp.models.entity.Visitante;
import eby.py.visitasrrpp.models.entity.Visitante.VisitanteBuilder;
import eby.py.visitasrrpp.models.entity.VisitanteGroup;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class VisitanteServiceImpl implements IVisitanteService {

	@Autowired
	private IVisitanteDao visitanteDao;
	@Autowired
	private IVisitanteGroupDao visitanteGroupDao;
	@Autowired
	private IImageSideDao imageSideDao;
	@Autowired
	private ITipoDocumentoDao tipoDocumentoDao;
	@Autowired
	private IPaisDao paisDao;
	@Autowired
	IVisitanteGroupDao groupDao;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	@Transactional(readOnly = true)
	public List<Visitante> findAll() {

		return (List<Visitante>) visitanteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Visitante findById(Long id) throws NotFoundException {

		return (Visitante) visitanteDao.findById(id).orElseThrow(() -> new NotFoundException());

	}

	@Override
	@Transactional
	public VisitanteDto saveWithDocs(VisitanteSaveRequest param) throws Exception {
		MultipartFile front = param.getIdDocFront();
		MultipartFile back = param.getIdDocBack();
		ImageSide idDocFrontSide = ImageSide.builder().name(StringUtils.cleanPath(front.getOriginalFilename()))
				.data(front.getBytes()).side("front").build();
		ImageSide idDocBackSide = ImageSide.builder().name(StringUtils.cleanPath(back.getOriginalFilename()))
				.data(back.getBytes()).side("back").build();
		ImageSide persistedFront = imageSideDao.save(idDocFrontSide);
		ImageSide persistedBack = imageSideDao.save(idDocBackSide);
		Visitante visitante = Visitante.builder()
				.nombre(param.getNombre())
				.apellido(param.getApellido())
				.nroDocumento(param.getNroDocumento())
				.email(param.getEmail())
				.sexo(param.getSexo())
				.tipoDocumento(tipoDocumentoDao.findById(param.getTipoDocumentoId()).get())
				.pais(paisDao.findById(param.getPaisId()).get())
				.backSide(persistedBack)
				.frontSide(persistedFront)
				.build();
		// .password(AESencryption.encrypt(param.getPassword()))
		// .codigoTelefono(param.getCodigoTelefono())
		// .group(groupDao.findById(param.getGroupId()).get())
		// .telefono(param.getTelefono())
		VisitanteDto result = visitanteDaoToDto(visitanteDao.save(visitante));
		result.setIdDocImageFrontSideUri(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/visitantes/files/id-document-side-image/")
						.path(persistedFront.getId().toString())
						.toUriString());
		result.setIdDocImageBackSideUri(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/visitantes/files/id-document-side-image/")
						.path(persistedBack.getId().toString())
						.toUriString());
		generateAndSendQrCode("PersonaId:" + result.getPersonaId() + ";NroDocumento:" + result.getNroDocumento(),
				visitante.getEmail(), result.getApellido() + "qrCode.png");
		return result;
	}

	@Override
	@Transactional
	public VisitanteDto saveByAdmin(VisitanteWithGroupeSaveRequest param) throws Exception {
		MultipartFile front = param.getIdDocFront();
		MultipartFile back = param.getIdDocBack();
		ImageSide idDocFrontSide = ImageSide.builder().name(StringUtils.cleanPath(front.getOriginalFilename()))
				.data(front.getBytes()).side("front").build();
		ImageSide idDocBackSide = ImageSide.builder().name(StringUtils.cleanPath(back.getOriginalFilename()))
				.data(back.getBytes()).side("back").build();
		ImageSide persistedFront = imageSideDao.save(idDocFrontSide);
		ImageSide persistedBack = imageSideDao.save(idDocBackSide);
		Visitante visitante = Visitante.builder()
				.nombre(param.getNombre())
				.apellido(param.getApellido())
				.nroDocumento(param.getNroDocumento())
				.email(param.getEmail())
				.sexo(param.getSexo())
				.tipoDocumento(tipoDocumentoDao.findById(param.getTipoDocumentoId()).get())
				.pais(paisDao.findById(param.getPaisId()).get())
				.backSide(persistedBack)
				.frontSide(persistedFront)
				.build();
		// .password(AESencryption.encrypt(param.getPassword()))
		// .codigoTelefono(param.getCodigoTelefono())
		// .group(groupDao.findById(param.getGroupId()).get())
		// .telefono(param.getTelefono())
		VisitanteDto result = visitanteDaoToDto(visitanteDao.save(visitante));
		result.setIdDocImageFrontSideUri(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/visitantes/files/id-document-side-image/")
						.path(persistedFront.getId().toString())
						.toUriString());
		result.setIdDocImageBackSideUri(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/visitantes/files/id-document-side-image/")
						.path(persistedBack.getId().toString())
						.toUriString());
		return result;
	}


	@Override
	@Transactional
	public VisitanteDto saveWithGroupeByAdmin(VisitanteWithGroupeSaveRequest param) throws Exception {
		MultipartFile front = param.getIdDocFront();
		MultipartFile back = param.getIdDocBack();
		ImageSide idDocFrontSide = ImageSide.builder().name(StringUtils.cleanPath(front.getOriginalFilename()))
				.data(front.getBytes()).side("front").build();
		ImageSide idDocBackSide = ImageSide.builder().name(StringUtils.cleanPath(back.getOriginalFilename()))
				.data(back.getBytes()).side("back").build();
		ImageSide persistedFront = imageSideDao.save(idDocFrontSide);
		ImageSide persistedBack = imageSideDao.save(idDocBackSide);
		Visitante visitante = Visitante.builder()
				.nombre(param.getNombre())
				.apellido(param.getApellido())
				.nroDocumento(param.getNroDocumento())
				.email(param.getEmail())
				.telefono(param.getTelefono())
				.sexo(param.getSexo())
				.tipoDocumento(tipoDocumentoDao.findById(param.getTipoDocumentoId()).get())
				.pais(paisDao.findById(param.getPaisId()).get())
				.backSide(persistedBack)
				.frontSide(persistedFront)
				.idDocImageFrontSideUri(
					ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/visitantes/files/id-document-side-image/")
						.path(persistedFront.getId().toString())
						.toUriString()
				)
				.idDocImageBackSideUri(
					ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/visitantes/files/id-document-side-image/")
						.path(persistedFront.getId().toString())
						.toUriString()
				)
				.group(groupDao.findById(param.getGroupeId()).get())
				.build();
		// .password(AESencryption.encrypt(param.getPassword()))
		// .codigoTelefono(param.getCodigoTelefono())
		// .telefono(param.getTelefono())
		VisitanteDto result = visitanteDaoToDto(visitanteDao.save(visitante));
		// System.out.println("telefono:"+result.getTelefono());
		// result.setIdDocImageFrontSideUri(
		// 		ServletUriComponentsBuilder
		// 				.fromCurrentContextPath()
		// 				.path("/api/visitantes/files/id-document-side-image/")
		// 				.path(persistedFront.getId().toString())
		// 				.toUriString());
		// result.setIdDocImageBackSideUri(
		// 		ServletUriComponentsBuilder
		// 				.fromCurrentContextPath()
		// 				.path("/api/visitantes/files/id-document-side-image/")
		// 				.path(persistedBack.getId().toString())
		// 				.toUriString());
		generateAndSendQrCode("Persona Id:" + result.getPersonaId() + "; Persona Nombre:" + result.getNombre() + "; Persona Appelido:" + result.getApellido() +"; NroDocumento:" + result.getNroDocumento(),
				visitante.getEmail(), result.getApellido().replace(" ", "_") + "-"+ result.getNombre() + "-QrCode.png");
		return result;
	}

	@Override
	@Transactional
	public VisitanteDto updateWithDocs(VisitanteWithGroupeSaveRequest param) throws Exception {
		MultipartFile front = param.getIdDocFront();
		MultipartFile back = param.getIdDocBack();
		ImageSide idDocFrontSide = null;
		ImageSide idDocBackSide = null;
		ImageSide persistedFront = null;
		ImageSide persistedBack = null;
		if (front != null & back != null) {
			idDocFrontSide = ImageSide.builder().name(StringUtils.cleanPath(front.getOriginalFilename()))
					.data(front.getBytes()).side("front").build();
			idDocBackSide = ImageSide.builder().name(StringUtils.cleanPath(back.getOriginalFilename()))
					.data(back.getBytes()).side("back").build();
			persistedFront = imageSideDao.save(idDocFrontSide);
			persistedBack = imageSideDao.save(idDocBackSide);
		}
		Visitante visitante = visitanteDao.findById(param.getPersonaId())
				.orElseThrow(() -> new Exception("Visitante not found"));
		if (persistedFront != null & persistedBack != null) {
			visitante.setNombre(param.getNombre());
			visitante.setApellido(param.getApellido());
			visitante.setNroDocumento(param.getNroDocumento());
			visitante.setEmail(param.getEmail());
			visitante.setSexo(param.getSexo());
			visitante.setTipoDocumento(tipoDocumentoDao.findById(param.getTipoDocumentoId()).get());
			visitante.setBackSide(persistedBack);
			visitante.setFrontSide(persistedFront);
			visitante.setPais(paisDao.findById(param.getPaisId()).get());
		}
		// .password(AESencryption.encrypt(param.getPassword()))
		// .codigoTelefono(param.getCodigoTelefono())
		// .group(groupDao.findById(param.getGroupId()).get())
		// .telefono(param.getTelefono())
		else {
			visitante.setNombre(param.getNombre());
			visitante.setApellido(param.getApellido());
			visitante.setNroDocumento(param.getNroDocumento());
			visitante.setEmail(param.getEmail());
			visitante.setSexo(param.getSexo());
			visitante.setTipoDocumento(tipoDocumentoDao.findById(param.getTipoDocumentoId()).get());
			visitante.setPais(paisDao.findById(param.getPaisId()).get());
		}
		VisitanteDto result = visitanteDaoToDto(visitanteDao.save(visitante));
		result.setIdDocImageFrontSideUri(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/visitantes/files/id-document-side-image/")
						.path(persistedFront.getId().toString())
						.toUriString());
		result.setIdDocImageBackSideUri(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/visitantes/files/id-document-side-image/")
						.path(persistedBack.getId().toString())
						.toUriString());
		return result;
	}


	@Override
	@Transactional
	public VisitanteDto updateWithGroupeAndDocs(VisitanteWithGroupeSaveRequest param) throws Exception {
		MultipartFile front = param.getIdDocFront();
		MultipartFile back = param.getIdDocBack();
		ImageSide idDocFrontSide = null;
		ImageSide idDocBackSide = null;
		ImageSide persistedFront = null;
		ImageSide persistedBack = null;
		if (front != null & back != null) {
			idDocFrontSide = ImageSide.builder().name(StringUtils.cleanPath(front.getOriginalFilename()))
					.data(front.getBytes()).side("front").build();
			idDocBackSide = ImageSide.builder().name(StringUtils.cleanPath(back.getOriginalFilename()))
					.data(back.getBytes()).side("back").build();
			persistedFront = imageSideDao.save(idDocFrontSide);
			persistedBack = imageSideDao.save(idDocBackSide);
		}
		Visitante visitante = visitanteDao.findById(param.getPersonaId())
				.orElseThrow(() -> new Exception("Visitante not found"));
		if (persistedFront != null & persistedBack != null) {
			visitante.setNombre(param.getNombre());
			visitante.setApellido(param.getApellido());
			visitante.setNroDocumento(param.getNroDocumento());
			visitante.setEmail(param.getEmail());
			visitante.setTelefono(param.getTelefono());
			visitante.setSexo(param.getSexo());
			visitante.setTipoDocumento(tipoDocumentoDao.findById(param.getTipoDocumentoId()).get());
			visitante.setBackSide(persistedBack);
			visitante.setFrontSide(persistedFront);
			visitante.setPais(paisDao.findById(param.getPaisId()).get());
			visitante.setGroup(groupDao.findById(param.getGroupeId()).get());
		}
		// .password(AESencryption.encrypt(param.getPassword()))
		// .codigoTelefono(param.getCodigoTelefono())
		// .telefono(param.getTelefono())
		else {
			visitante.setNombre(param.getNombre());
			visitante.setApellido(param.getApellido());
			visitante.setNroDocumento(param.getNroDocumento());
			visitante.setEmail(param.getEmail());
			visitante.setTelefono(param.getTelefono());
			visitante.setSexo(param.getSexo());
			visitante.setTipoDocumento(tipoDocumentoDao.findById(param.getTipoDocumentoId()).get());
			visitante.setPais(paisDao.findById(param.getPaisId()).get());
			visitante.setGroup(groupDao.findById(param.getGroupeId()).get());
		}
		// System.out.println("the visitante object being saved is visitante:"+visitante.toString());
		VisitanteDto result = visitanteDaoToDto(visitanteDao.save(visitante));
		// result.setIdDocImageFrontSideUri(
		// 		ServletUriComponentsBuilder
		// 				.fromCurrentContextPath()
		// 				.path("/api/visitantes/files/id-document-side-image/")
		// 				.path(persistedFront.getId().toString())
		// 				.toUriString());
		// result.setIdDocImageBackSideUri(
		// 		ServletUriComponentsBuilder
		// 				.fromCurrentContextPath()
		// 				.path("/api/visitantes/files/id-document-side-image/")
		// 				.path(persistedBack.getId().toString())
		// 				.toUriString());
		return result;
	}

	private void generateAndSendQrCode(String barcodeText, String email, String filename)
			throws WriterException, MessagingException, IOException {
		// TODO Auto-generated method stub
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
		BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject("Registration Confirmation, with QR code");
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

		// Create MimeMultipart and text part
		MimeMultipart multipart = new MimeMultipart();
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(
				"This email is a confirmatin about your registration success. and you will find attached your QR code",
				"text/plain");
		multipart.addBodyPart(textPart);

		// Convert BufferedImage to byte[]
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(qrCodeImage, "png", baos);
		byte[] imageBytes = baos.toByteArray();
		baos.close();

		// Create image part
		MimeBodyPart imagePart = new MimeBodyPart();
		imagePart.setContent(imageBytes, "image/png");
		imagePart.setDisposition(MimeBodyPart.ATTACHMENT); // Set attachment disposition
		imagePart.setFileName(filename);
		multipart.addBodyPart(imagePart);

		// Set message content
		message.setContent(multipart);

		// Send email
		mailSender.send(message);
	}

	@Override
	@Transactional
	public Visitante save(Visitante visitante) {
		return visitanteDao.save(visitante);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws NotFoundException {

		findById(id);
		visitanteDao.deleteById(id);

	}

	@Override
	public Visitante addToGroup(Long visitanteId, Long GroupId) throws NoSuchElementException {
		// TODO Auto-generated method stub
		Visitante visitante = visitanteDao.findById(visitanteId).get();
		VisitanteGroup group = visitanteGroupDao.findById(GroupId).get();
		if (visitante != null && group != null) {
			visitante.setGroup(group);
			return visitanteDao.save(visitante);
		}
		throw new NoSuchElementException();
	}

	@Override
	public List<Visitante> findByGroup(Long groupId) {
		// TODO Auto-generated method stub
		return visitanteDao.findByGroupId(groupId);
	}

	public ResponseEntity<byte[]> generateGroupeReport(Long groupeId) throws IOException, JRException {

		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(
				visitanteDao.findByGroupId(groupeId), true);

		Map<String, Object> parameters = new HashMap<>();

		String path = resourceLoader.getResource("classpath:visitorsOfGroupe.jrxml").getURI().getPath();

		JasperReport compileReport = JasperCompileManager
				.compileReport(new FileInputStream(path));

		JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

		byte data[] = JasperExportManager.exportReportToPdf(jasperPrint);

		System.err.println(data);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=visitorsOfGroup.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
	}

	@Override
	public ImageSide getImageSide(Long id) {
		// TODO Auto-generated method stub
		return imageSideDao.findById(id).get();
	}

	private VisitanteDto visitanteDaoToDto(Visitante visitante) {
		return mapper.map(visitante, VisitanteDto.class);
	}

}

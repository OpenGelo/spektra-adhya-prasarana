package app.configs;

import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

@Component
public class PrintConfig {

	private DataSource dataSource;
	private JasperDesign design;
	private JasperReport report;
	private JasperPrint print;
	private JasperViewer viewer;

	public JasperDesign getDesign() {
		return design;
	}

	public void setDesign(JasperDesign design) {
		this.design = design;
	}

	public JasperReport getReport() {
		return report;
	}

	public void setReport(JasperReport report) {
		this.report = report;
	}

	public JasperPrint getPrint() {
		return print;
	}

	public void setPrint(JasperPrint print) {
		this.print = print;
	}

	public JasperViewer getViewer() {
		return viewer;
	}

	public void setViewer(JasperViewer viewer) {
		this.viewer = viewer;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 1. lakukan load file .jrxml
	 * 
	 * @param jrxml
	 * @throws JRException
	 */
	public void setDesign(String jrxml) throws JRException {
		this.design = JRXmlLoader.load(getClass().getResourceAsStream(jrxml));
	}

	/**
	 * 2. lakukan comipilasi file .jrxml menjadi .jasper
	 * 
	 * @param design
	 * @throws JRException
	 */
	public void setReport(JasperDesign design) throws JRException {
		this.report = JasperCompileManager.compileReport(design);
	}

	/**
	 * 3. isi data menggunakan parameter atau JRBeanCollection
	 * 
	 * @param report
	 * @param map
	 * @param collection
	 * @throws JRException
	 */
	public void setPrint(JasperReport report, HashMap<String, Object> map, JRBeanCollectionDataSource collection)
			throws JRException {
		this.print = JasperFillManager.fillReport(report, map, collection);
	}

	/**
	 * 3. isi data menggunakan parameter aja
	 * 
	 * @param report
	 * @param map
	 * @param collection
	 * @throws JRException
	 */
	public void setPrint(JasperReport report, HashMap<String, Object> map)
			throws JRException {
		this.print = JasperFillManager.fillReport(report, map, new JREmptyDataSource());
	}

	public void setPrint(JasperReport report, HashMap<String, Object> map, DataSource dataSource)
			throws SQLException, JRException {
		this.print = JasperFillManager.fillReport(report, map, dataSource.getConnection());
	}

	public void setViewer(JasperPrint print, String title) {
		this.viewer = new JasperViewer(print, false);
		this.viewer.setSize(600, 400);
		this.viewer.setTitle(title);
		this.viewer.setFitWidthZoomRatio();
		this.viewer.setVisible(true);
		this.viewer.requestFocus();
	}

	public void setPrinted(JasperPrint print, String title) throws JRException {
		JasperPrintManager.printReport(print, false);
	}

}

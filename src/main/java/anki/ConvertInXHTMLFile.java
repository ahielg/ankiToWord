package anki;

import java.io.File;

import jakarta.xml.bind.JAXBException;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.ImportXHTMLProperties;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.RFonts;

public class ConvertInXHTMLFile {

    public static void main(String[] args) throws Exception {
        //ConvertInXHTMLFile.createDocx("<b>שלום</b> אחיאל!!!");
        ConvertInXHTMLFile.createDocx("ששששש <strong>שלום</strong>");

    }

    public static void createDocx(String html) throws JAXBException, Docx4JException {
        String unescaped = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\n" +
                "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" dir=\"rtl\" lang=\"he\">\n" +
                "<head>\n" +
                "  <title>Title of document</title>\n" +
                "</head>\n" +
                "<body dir=\"rtl\">\n" +
                "\n" +
                html +
                "\n" +
                "</body>\n" +
                "</html>";

        ImportXHTMLProperties.setProperty("docx4j-ImportXHTML.Bidi.Heuristic", true);
        createDocx("//", unescaped);
    }

    public static void createDocx(String baseURL, String html) throws JAXBException, Docx4JException {
        System.out.println("Unescaped: " + html);


        // Setup font mapping
        RFonts rfonts = Context.getWmlObjectFactory().createRFonts();
        rfonts.setAscii("Century Gothic");
        XHTMLImporterImpl.addFontMapping("Century Gothic", rfonts);

        // Create an empty docx package
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
//		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(System.getProperty("user.dir") + "/styled.docx"));


        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
        wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
        ndp.unmarshalDefaultNumbering();

        // Convert the XHTML, and add it into the empty docx we made
        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);

        XHTMLImporter.setHyperlinkStyle("Hyperlink");
        wordMLPackage.getMainDocumentPart().getContent()
                .addAll(XHTMLImporter.convert(html, baseURL));

        System.out.println(
                XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));

//		System.out.println(
//				XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getNumberingDefinitionsPart().getJaxbElement(), true, true));

        String fileLocation = "c://Users/User/Downloads";

        wordMLPackage.save(new File(fileLocation + "/OUT_from_XHTML.docx"));
        //wordMLPackage.save(new File(System.getProperty("user.dir") + "/OUT_from_XHTML.docx"));
    }

}
package dailymarket.swing.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;

public class PrinterTicket implements Printable{
	protected String[] ticketString;
	 
	public PrinterTicket(String[] s){
		ticketString = s;
	}
	
	public int print(Graphics g,PageFormat pf, int page){
		if(page == 0 ){
			
		
		final int PULGADA = 17;
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(pf.getImageableX(),pf.getImageableY());
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke(2));
			
		Point2D.Double punto = new Point2D.Double(0.25 * PULGADA, 0.25 * PULGADA);

		double ancho = 8 * PULGADA;

//		AttributedString parrafo = new AttributedString(ticketString);
//		parrafo.addAttribute(TextAttribute.FONT,new Font("Serif", Font.PLAIN, 8));
//		LineBreakMeasurer lineaBreaker = new LineBreakMeasurer(parrafo.getIterator(),new FontRenderContext(null,true,true));

		TextLayout Campo;
		TextLayout justCampo;
		
				
		Font f = new Font( "Times",Font.BOLD,10 );
		
		Vector lineas = new Vector();
		
		for(int con = 0; con < ticketString.length ;con++  ){
			TextLayout tl = new TextLayout( ticketString[con],f,new FontRenderContext(null,true,true) );	
			lineas.add(tl);
		}
		
		for (int i = 0; i < lineas.size(); i++) {
		Campo = (TextLayout)lineas.get(i);

		if (i != lineas.size()-1) {
		justCampo = Campo.getJustifiedLayout((float)ancho);

			} else {
			justCampo = Campo;
			}

		punto.y += justCampo.getAscent();
		justCampo.draw(g2d,(float)punto.x,(float)punto.y);

		punto.y += justCampo.getDescent() + justCampo.getLeading();
		}
		
		return (PAGE_EXISTS);
		}
		else
			return NO_SUCH_PAGE;
}


}
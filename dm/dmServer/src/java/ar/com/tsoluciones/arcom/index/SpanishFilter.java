package ar.com.tsoluciones.arcom.index;

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * TokenFilter para espa�ol.
 * Saca puntos de acr�nimos, saca acentos, di�resis y pasa a min�sculas. 
 */
public final class SpanishFilter extends TokenFilter {

	public SpanishFilter(TokenStream in) {
		super(in);
	}

	/** 
	 * Returns the next token in the stream, or null at EOS.
	 * <p>Removes <tt>'s</tt> from the end of words.
	 * <p>Removes dots from acronyms.
	 */
	@Override
	public final Token next() throws IOException {
		Token t = input.next();
		if (t == null)
			return null;

		String text = new String(t.termBuffer(), 0, t.termLength());
		String type = t.type();

		// remove dots
		if (type == StandardTokenizer.TOKEN_TYPES[StandardTokenizer.ACRONYM]) { 
			StringBuilder trimmed = new StringBuilder();
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if (c != '.')
					trimmed.append(c);
			}
			text = trimmed.toString();
		}
		return new Token(canonizar(text), t.startOffset(), t.endOffset(), type);
	}
	
	public static String canonizar(String token) {
		String lowerCase = token.toLowerCase();
		String withoutAcute = lowerCase.replace('�', 'a').replace('�', 'e').
			replace('�', 'i').replace('�', 'o').replace('�', 'u').replace("�", "n");
		return sacarDiacriticos(withoutAcute);
	}
	
	/**
	 * Elimina marcas diacr�ticas de las letras (excepto para la �, que es
	 * considerada una letra diferente en espa�ol)
	 */
	public static String sacarDiacriticos(String s) {
		return s.replaceAll("[[\\p{Mn}\\p{Me}\\p{Punct}\\u00a1\\u00bf]&&[^\\u0303]]", "");
	}
	
	public static void main(String[] args) {
		System.out.println(canonizar("�Algo �?"));
	}
}

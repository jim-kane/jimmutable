package org.kane.blendr.parse;

import org.kane.base.serialization.Validator;
import org.kane.blendr.lex.LexOutput;
import org.kane.blendr.lex.Lexer;
import org.kane.blendr.lex.Token;
import org.kane.blendr.lex.TokenCloseTag;
import org.kane.blendr.lex.TokenOpenTag;
import org.kane.blendr.lex.TokenContent;

/**
 * A class that encapsulates the blendr parser
 * 
 * @author jim.kane
 *
 */
public class Parser 
{
	/**
	 * Parse source code into a ParseTree
	 * 
	 * @param original_source_code The source code of the blendr program
	 * @return a ParseTree representing the parsed source_code
	 * @throws ParseError.Exception 
	 */
	static public ParseTree parse(String original_source_code) throws ParseError.Exception
	{
		LexOutput lex = Lexer.lex(original_source_code);
		
		ParseTree.Builder builder = new ParseTree.Builder();
		
		for ( Token token : lex.getSimpleTokens() )
		{
			if ( token instanceof TokenContent ) 
			{
				builder.addTextNode((TokenContent)token);
				continue;
			}
			
			if ( token instanceof TokenOpenTag )
			{
				builder.openTag((TokenOpenTag)token);
				continue;
			}
			
			if ( token instanceof TokenCloseTag )
			{
				builder.closeTag((TokenCloseTag)token);
				continue;
			}
			
			ParseError.ERROR_UNKNOWN_TOKEN_TYPE.throwException(token);
		}
		
		return builder.create();
	}

}

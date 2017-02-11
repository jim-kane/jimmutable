package org.kane.blendr.parse;

import java.util.List;

import org.kane.base.serialization.Validator;
import org.kane.blendr.execute.Executor;
import org.kane.blendr.lex.Tag;

/**
 * A special parse tree node used to represent an entire program (a.k.a.
 * document)
 * 
 * This node may only be a root node (i.e. it can not have a parent)
 * 
 * @author jim.kane
 *
 */
public class ParseTreeProgram extends ParseTree
{
	protected ParseTreeProgram(ParseTree.Builder builder)
	{
		super(builder, null);
	}
	
	public ParseTreeProgram(List<ParseTree> children)
	{
		super(null, children);
		
		complete();
	}

	@Override
	public String getToStringLabel()
	{
		return "{document}";
	}

	@Override
	public boolean nodeEquals(ParseTree node) 
	{
		return node instanceof ParseTreeProgram;
	}
	
}

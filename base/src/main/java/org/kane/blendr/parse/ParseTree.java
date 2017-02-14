package org.kane.blendr.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.serialization.Equality;
import org.kane.base.serialization.Validator;
import org.kane.blendr.lex.TokenCloseTag;
import org.kane.blendr.lex.TokenContent;
import org.kane.blendr.lex.TokenOpenTag;

/**
 * The abstract base class used to represent a blendr parse tree
 * 
 * @author jim.kane
 *
 */
abstract public class ParseTree extends StandardImmutableObject
{
	private ParseTree parent = null;
	private FieldList<ParseTree> children = new FieldArrayList();

	protected ParseTree(Builder builder, ParseTree parent)
	{
		this.parent = parent;
	}
	
	public ParseTree(ParseTree parent, List<ParseTree> children)
	{
		this.parent = parent;
		this.children = new FieldArrayList(children);
	}
	
	public void validate()
	{
		Validator.notNull(children);
		Validator.containsNoNulls(children);
	}
	
	public void normalize()
	{
		
	}
	
	public void freeze()
	{
		children.freeze();
	}
	
	public boolean hasParent() { return parent != null; }
	
	public ParseTree getOptionalParent(ParseTree default_value) 
	{
		if ( !hasParent() ) return default_value;
		return parent; 
	}
	
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		
		for ( int i = 0; i < getSimpleDepth(); i++ )
		{
			ret.append("\t");
		}
		
		ret.append(getToStringLabel());
		ret.append("\n");
		
		for ( ParseTree child : children )
		{
			ret.append(child.toString());
		}
		
		
		
		return ret.toString();
	}
	
	/**
	 * Get the "depth" of this node of the parse tree
	 * @return The depth (0 ... n) of this node of the parse tree
	 */
	public int getSimpleDepth()
	{
		if ( parent == null ) return 0;
		
		return parent.getSimpleDepth()+1;
	}
	
	public List<ParseTree> getSimpleChildren() { return children; }
	
	/**
	 * Returns a nice, human readable, one line description of this node (e.g.
	 * CONTENT, {script}, etc.)
	 * 
	 * @return A human readable description of this node
	 */
	abstract public String getToStringLabel();
	
	/**
	 * The number of operands (i.e. nops) of this node. 0...n
	 * 
	 * @return The number of operands (children) of this node of the parse tree
	 */
	public int nops() { return children.size(); }
	
	/**
	 * Get the specified operand (0...n)
	 * 
	 * @param idx
	 *            The index of the operand
	 * @return The operand (throws an execption of idx is invalid)
	 */
	public ParseTree op(int idx) { return children.get(idx); }
	

	public int compareTo(Object o) 
	{
		return Integer.compare(hashCode(), o.hashCode()); // this produces an ordering to parse trees... its just not a very natural one...
	}

	public int hashCode() 
	{
		int ret = 0;
		if ( hasParent() ) ret += getOptionalParent(null).hashCode();
		
		ret += children.size();
		
		for ( ParseTree child : children ) ret += child.hashCode();
		
		return ret;
	}
	
	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof ParseTree) ) return false;
		
		ParseTree o = (ParseTree)obj;
		
		if ( !nodeEquals(o) ) return false;
		if ( !Equality.optionalEquals(getOptionalParent(null), o.getOptionalParent(null)) ) return false;
		
		return children.equals(o.children);
	}
	
	abstract public boolean nodeEquals(ParseTree node);


	/**
	 * A class for building parse trees.  Used extensively by Parser
	 * 
	 * @author jim.kane
	 *
	 */
	static public class Builder
	{
		ParseTreeProgram root;
		ParseTree cursor;
		
		public Builder()
		{
			root = new ParseTreeProgram(this);
			cursor = root;
		}
		
		public void addTextNode(TokenContent text) throws ParseError.Exception
		{
			ParseTreeContent content = new ParseTreeContent(cursor,text.getSimpleText());
			((ParseTree)cursor).children.add(content);
		}
		
		public void openTag(TokenOpenTag open_operator) throws ParseError.Exception
		{
			ParseTreeTag new_under_construction = new ParseTreeTag(this, cursor, open_operator.getSimpleOperator());
			
			cursor.children.add(new_under_construction);
			
			cursor = new_under_construction;
		}
		
		public void closeTag(TokenCloseTag close_operator) throws ParseError.Exception
		{
			if ( cursor.parent == null || !(cursor instanceof ParseTreeTag) )
				ParseError.ERROR_MIS_MATCHED_CLOSING_TAG.throwException(close_operator);
			
			ParseTreeTag tag_at_cursor = (ParseTreeTag)cursor;
			
			if ( !tag_at_cursor.getSimpleType().equals(close_operator.getSimpleOperator()) )
			{
				ParseError.ERROR_MIS_MATCHED_CLOSING_TAG.throwException(close_operator);
			}
			
			cursor.complete();
			
			cursor = cursor.getOptionalParent(null);
		}
		
		public ParseTree create() throws ParseError.Exception
		{
			if ( cursor != root )
				ParseError.ERROR_MISSING_CLOSING_TAG.throwException(null);
				
			root.complete();
			
			return root;
		}
	}
}

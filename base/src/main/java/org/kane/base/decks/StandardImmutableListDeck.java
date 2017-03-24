package org.kane.base.decks;

import org.kane.base.fields.FieldList;

abstract public class StandardImmutableListDeck<T extends StandardImmutableListDeck<T, E>, E> extends StandardImmutableDeck<T, E>
{
	@Override
    abstract public FieldList<E> getSimpleContents();
}

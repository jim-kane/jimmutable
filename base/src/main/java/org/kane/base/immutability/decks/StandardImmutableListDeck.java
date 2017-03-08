package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldList;

abstract public class StandardImmutableListDeck<T extends StandardImmutableListDeck<T, E>, E> extends StandardImmutableDeck<T, E>
{
	@Override
    abstract public FieldList<E> getSimpleContents();
}

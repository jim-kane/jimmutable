package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldList;

abstract public class StandardImmutableJeffDeckList<T extends StandardImmutableJeffDeckList<T, E>, E> extends StandardImmutableJeffDeck<T, E>
{
	@Override
    abstract public FieldList<E> getSimpleContents();
}

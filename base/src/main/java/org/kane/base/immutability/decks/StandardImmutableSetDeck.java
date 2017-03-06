package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldSet;

abstract public class StandardImmutableSetDeck<T extends StandardImmutableSetDeck<T, E>, E> extends StandardImmutableDeck<T, E>
{
	@Override
    abstract public FieldSet<E> getSimpleContents();
}

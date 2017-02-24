package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldSet;

abstract public class StandardImmutableJeffDeckSet<T extends StandardImmutableJeffDeckSet<T, E>, E> extends StandardImmutableJeffDeck<T, E>
{
	@Override
    abstract public FieldSet<E> getSimpleContents();
}

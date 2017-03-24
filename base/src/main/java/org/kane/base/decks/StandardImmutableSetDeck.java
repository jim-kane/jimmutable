package org.kane.base.decks;

import org.kane.base.fields.FieldSet;

abstract public class StandardImmutableSetDeck<T extends StandardImmutableSetDeck<T, E>, E> extends StandardImmutableDeck<T, E>
{
	@Override
    abstract public FieldSet<E> getSimpleContents();
}

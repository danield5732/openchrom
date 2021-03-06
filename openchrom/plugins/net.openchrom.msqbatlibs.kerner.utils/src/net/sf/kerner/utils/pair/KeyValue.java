/*******************************************************************************
 * Copyright (c) 2015, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Alexander Kerner - initial API and implementation
 *******************************************************************************/
package net.sf.kerner.utils.pair;

import java.util.Map.Entry;

/**
 * 
 * Simple key-value mapping.
 * <p>
 * {@code key} must not be {@code null}; {@code value} may be {@code null}.
 * </p>
 * 
 * <p>
 * <b>Example:</b><br>
 * 
 * </p>
 * <p>
 * 
 * <pre>
 * TODO example
 * </pre>
 * 
 * </p>
 * <p>
 * <b>Threading:</b><br>
 * 
 * </p>
 * <p>
 * 
 * <pre>
 * Not thread save.
 * </pre>
 * 
 * </p>
 * <p>
 * last reviewed: 0000-00-00
 * </p>
 * 
 * @author <a href="mailto:alexanderkerner24@gmail.com">Alexander Kerner</a>
 * 
 * @param <K>
 *            type of first object
 * @param <V>
 *            type of second object
 */
public class KeyValue<K, V> implements Entry<K, V>, Pair<K, V> {

	/**
	 * 
	 */
	private final K key;
	/**
	 * 
	 */
	private volatile V value;

	/**
	 * Create a new {@code KeyValue} object, using given key.
	 * 
	 * @param key
	 *            key for this key-value-mapping
	 * @throws NullPointerException
	 *             if given {@code key} is {@code null}
	 */
	public KeyValue(final K key) {
		if(key == null)
			throw new NullPointerException("key must not be null");
		this.key = key;
		this.value = null;
	}

	/**
	 * Create a new {@code KeyValue} object, using given key and value.
	 * 
	 * @param key
	 *            key for this key-value-mapping
	 * @param value
	 *            value for this key-value-mapping
	 * @throws NullPointerException
	 *             if given {@code key} is {@code null}
	 */
	public KeyValue(final K key, final V value) {
		if(key == null)
			throw new NullPointerException("key must not be null");
		this.key = key;
		this.value = value;
	}

	/**
	 * Create a new {@code KeyValue} object, using given {@code KeyValue} as a
	 * template.
	 * 
	 * @param template
	 *            {@code KeyValue} template
	 */
	public KeyValue(final KeyValue<K, V> template) {
		this(template.getKey(), template.getValue());
	}

	public KeyValue<K, V> clone() {

		return new KeyValue<K, V>(getKey(), getValue());
	}

	/**
	 * In contrast to {@link Pair}, {@code KeyValue(1,2)} does not equal {@code KeyValue(2,1)}.
	 * 
	 */
	public boolean equals(final Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof KeyValue))
			return false;
		@SuppressWarnings("rawtypes")
		final KeyValue other = (KeyValue)obj;
		if(key == null) {
			if(other.key != null)
				return false;
		} else if(!key.equals(other.key))
			return false;
		if(value == null) {
			if(other.value != null)
				return false;
		} else if(!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * Same as {@link KeyValue#getKey()}.
	 */
	public K getFirst() {

		return getKey();
	}

	/**
	 * Return the key for this key-value-mapping
	 */
	public K getKey() {

		return key;
	}

	/**
	 * Same as {@link KeyValue#getValue()}.
	 */
	public V getSecond() {

		return getValue();
	}

	/**
	 * Return the value for this key-value-mapping
	 */
	public V getValue() {

		return value;
	}

	/**
	 * In contrast to {@link Pair}, {@code KeyValue(1,2).hashCode()} does
	 * not equal {@code KeyValue(2,1).hashCode()}.
	 * 
	 */
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public KeyValue<V, K> invert() {

		return new KeyValue<V, K>(getValue(), getKey());
	}

	/**
	 * Set the value for this key-value-mapping, return the previous value
	 * mapped by this key-value-mapping
	 */
	public V setValue(final V value) {

		final V result = this.value;
		this.value = value;
		return result;
	}

	public String toString() {

		return key + "=" + value;
	}
}

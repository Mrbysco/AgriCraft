package com.agricraft.agricraft.api.adapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Interface for determining the true value of certain objects.
 *
 * @param <T> The type adapted by this adapter.
 */
public interface IAgriAdapter<T> {

	/**
	 * Determines if this adapter is capable of converting the given object to the target type.
	 *
	 * @param obj The object that needs to be converted.
	 * @return {@literal true} if this adapter can convert the given object to the target type,
	 * {@literal false} otherwise.
	 */
	boolean accepts(@Nullable Object obj);

	/**
	 * Converts the given object to the target type of this adapter, or returns the empty optional.
	 * <p>
	 * Notice, implementations of this method should never return null, instead the method should
	 * return {@link Optional#empty()}.
	 *
	 * @param obj
	 * @return
	 */
	@NotNull
	Optional<T> valueOf(@Nullable Object obj);

}

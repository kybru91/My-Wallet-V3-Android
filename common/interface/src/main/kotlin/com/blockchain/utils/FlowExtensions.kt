package com.blockchain.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

fun <T, R> Flow<Iterable<T>>.mapList(transform: (T) -> R): Flow<List<R>> = map { list ->
    list.map(transform)
}

fun <T, R> Flow<Iterable<T>>.mapListNotNull(transform: (T) -> R?): Flow<List<R>> = map { list ->
    list.mapNotNull(transform)
}

fun <T> Flow<Iterable<T>>.filterList(predicate: (T) -> Boolean): Flow<List<T>> = map { list ->
    list.filter(predicate)
}

inline fun <reified R> Flow<Iterable<*>>.filterListItemIsInstance(): Flow<List<R>> = map { list ->
    list.filterIsInstance<R>()
}

fun <T1, T2, T3, T4, T5, T6, R> combineMore(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> = combine(flow, flow2, flow3, flow4, flow5, flow6) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6,
    )
}

fun <T1, T2, T3, T4, T5, T6, T7, R> combineMore(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> = combine(flow, flow2, flow3, flow4, flow5, flow6, flow7) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6,
        args[6] as T7,
    )
}

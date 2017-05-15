export class MapUtils {
    static extend<K, T>(extendedMap: Map<K, T>, ...extensions: Map<K, T>[]) {
        for (var map of extensions) {
            map.forEach((value, key) => extendedMap.set(key, value));
        }
    }
}
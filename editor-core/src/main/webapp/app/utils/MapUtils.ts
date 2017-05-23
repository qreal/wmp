export class MapUtils {
    static extend<K, T>(extendedMap: Map<K, T>, ...extensions: Map<K, T>[]) {
        for (var map of extensions) {
            map.forEach((value, key) => extendedMap.set(key, value));
        }
    }

    static getFirstKey<K, T>(map: Map<K, T>): K {
        return map.keys().next().value;
    }

    static getFirstValue<K, T>(map: Map<K, T>): T {
        return map.values().next().value;
    }
}
class Map<T> {
    [key: string]: T;

    public static unite<T>(toMap: Map<T>, fromMap: Map<T>): void {
        for (var key in fromMap)
            toMap[key] = fromMap[key];
    }
}
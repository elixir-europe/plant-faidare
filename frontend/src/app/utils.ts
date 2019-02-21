export function asArray<T>(obj: T | T[]): T[] {
    if (!obj) {
        return null;
    }
    if (Array.isArray(obj)) {
        return obj;
    }
    return [obj];
}

export class KeyValueObject {
    public key: string;
    public value: string;

    constructor(key: string, value: string) {
        this.key = key;
        this.value = value;
    }

    static fromObject(o: { [key: string]: string }): KeyValueObject[] {
        return Object.entries(o)
            .filter(([key, value]) => !!key && !!value)
            .map(([key, value]) => new KeyValueObject(key, value));
    }
}


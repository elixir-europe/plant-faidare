import { KeyValueObject, toKeyValueObjects } from './utils';


describe('KeyValueObject', () => {

    it('should convert JS object to array of KeyValueObject', () => {
        const actual = toKeyValueObjects({
            'a': '1',
            'b': '2',
            'c': null,
            'd': '',
            '': 'e',
            'f': '3'
        });

        const expected: KeyValueObject[] = [
            { key: 'a', value: '1' },
            { key: 'b', value: '2' },
            { key: 'f', value: '3' },
        ];

        expect(actual).toEqual(expected);
    });

});

export default class AdTextParser {
    constructor() {
        this._result = '';

        this.options = {};

        this.initializeConverterMap();
    }

    setText(text) {
        this._text = text;
    }

    initializeConverterMap() {
        this._converterMap = {
            '*': this.handleBold,
            '_': this.handleItalic,
            'default': this.handleDefault
        };
    }

    convertToHTML() {
        this.appendToResult('<span>');

        let textLength = this._text.length;
        let idx = 0;

        while (idx < textLength) {
            let char = this._text[idx];

            let converter = this._converterMap[char] || this._converterMap['default'];
            converter.bind(this, char, idx)();

            idx++;
        }

        this.appendToResult('</span>');

        console.log(this._result);
        return this._result;
    }

    handleDefault(char, pos) {
        this.appendToResult(char);
    }

    handleBold(char, pos) {

    }

    handleItalic(char, pos) {

    }

    appendToResult(append) {
        this._result += append;
    }
}


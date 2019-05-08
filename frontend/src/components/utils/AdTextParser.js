export default class AdTextParser {
    charsToEscape = {
        '<': '&lt;',
        '>': '&gt;',
        '\'': '&#39;',
        '&': '&amp;',
        '"': '&quot;',
        '/': '&#47;'
    };

    constructor() {
        this._result = '';
        this._pos = 0;

        this._options = {
            bold: false,
            italic: false
        };

        this.initializeConverterMap();
    }

    setText(text) {
        this._text = text;
    }

    initializeConverterMap() {
        this._converterMap = {
            '<': this.handleEscapeChar,
            '>': this.handleEscapeChar,
            '\'': this.handleEscapeChar,
            '&': this.handleEscapeChar,
            '"': this.handleEscapeChar,
            '/': this.handleEscapeChar,
            '*': this.handleBold,
            '_': this.handleItalic,
            'default': this.handleDefault
        };
    }

    convertToHTML() {
        this.appendToResult('<span>');

        let textLength = this._text.length;

        while (this._pos < textLength) {
            let char = this._text[this._pos];

            let converter = this._converterMap[char] || this._converterMap['default'];
            converter.bind(this, char)();

            this._pos++;
        }

        this.appendToResult('</span>');

        console.log(this._result);
        return this._result;
    }

    handleDefault(char) {
        this.appendToResult(char);
    }

    handleBold() {
        this.handleSimpleTag('*', 'b', 'bold');
    }

    handleItalic() {
        this.handleSimpleTag('_', 'i', 'italic');
    }

    handleSimpleTag(targetedChar, tag, name) {
        if (this.getCharAt(this._pos) === targetedChar && this.getCharAt(this._pos + 1) === targetedChar) {
            this._options[name] ? this.appendToResult(`</${tag}>`) : this.appendToResult(`<${tag}>`);
            this._options[name] = !this._options[name];

            this._pos++;
        } else {
            this.appendToResult(targetedChar);
        }
    }

    handleEscapeChar(char) {
        this.appendToResult(this.charsToEscape[char]);
    }

    getCharAt(pos) {
        return this._text[pos];
    }

    appendToResult(append) {
        this._result += append;
    }
}


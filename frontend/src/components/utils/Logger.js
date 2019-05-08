class Logger {
    debug = true;

    log(msg) {
        if (this.debug)
            console.log("[AdExchange] " + msg);
    }
}

export default Logger = new Logger();

const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

export default (config, env, helpers) => {
    const uglifyJsPlugin = helpers.getPluginsByName(config, 'UglifyJsPlugin')[0];

    if(uglifyJsPlugin){
        const {index} = uglifyJsPlugin;

        config.plugins.splice(index, 1);
        config.plugins.push(new UglifyJsPlugin());
    }
};
import {render} from 'preact';
import {Router} from 'preact-router';

import App from './components/App';

render(
    <Router>
        <App path="/"/>
    </Router>, document.body);
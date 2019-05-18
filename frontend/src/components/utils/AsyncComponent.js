import {Component} from "preact";

// https://scotch.io/tutorials/lazy-loading-routes-in-react
export default function asyncComponent(getComponent) {
    class AsyncComponent extends Component {
        static Component = null;

        constructor(props) {
            super(props);

            this.state = {Component: AsyncComponent.Component};
        }

        componentWillMount() {
            if (!this.state.Component) {
                getComponent().then(Component => {
                    AsyncComponent.Component = Component;
                    this.setState({Component})
                })
            }
        }

        render() {
            const { Component } = this.state;

            if (Component) {
                return (<Component/>)
            }
            return null
        }
    }

    return AsyncComponent;
}
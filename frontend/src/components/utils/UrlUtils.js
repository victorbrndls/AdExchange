export function exact(path) {
    return getCurrentPath() === path;
}

export function include(path) {
    return getCurrentPath().includes(path);
}

export function not(path) {
    return getCurrentPath() !== path;
}

function getCurrentPath() {
    return location.pathname;
}

export default {
    exact,
    include,
    not,
    getCurrentPath
}
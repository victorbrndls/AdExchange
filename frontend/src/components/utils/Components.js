const LeftArrow = ({cb}) => (
    <div style="position: absolute;">
        <img id="websiteBackIcon" src="/assets/left-arrow.png"
        onClick={() => cb()}/>
    </div>
);

export {
    LeftArrow
}
import * as React from "react";
import "./TopBar.css";

export function TopBar(props: any) {
    const tabs = props.tabs.map((tab, index) => <li key={index}><a href={tab.link}>{tab.title}</a></li>);
    return (
        <div className="header-background">
            <h3 className="top-bar-header">Secret agency</h3>
            <ul>
                {tabs}
            </ul>
        </div>
    );
}
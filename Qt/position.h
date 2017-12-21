#ifndef POSITION_H
#define POSITION_H

class Position
{
public:
    explicit Position();
    explicit Position(double lat, double lon);
    Position(const Position& other);
    virtual ~Position();

    Position& operator= (const Position& other);

    bool isValid() const {
        return mLat >= -90.0 && mLat <= 90.0 && mLon >= -180 && mLon <= 180;
    }

    double getLat() const {
        return mLat;
    }

    double getLon() const {
        return mLon;
    }

    void setLat(double lat) {
        mLat = lat;
    }

    void setLon(double lon) {
        mLon = lon;
    }

    double getDistance(const Position &other) const;

private:
    double mLat;
    double mLon;
};

#endif // POSITION_H
